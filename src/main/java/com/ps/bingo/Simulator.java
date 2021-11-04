package com.ps.bingo;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.ps.bingo.exception.BingoException;
import com.ps.bingo.exception.ErrorCallback;
import com.ps.bingo.game.Game;
import com.ps.bingo.game.GameSettings;
import com.ps.bingo.game.LockManager;
import com.ps.bingo.localized.BundleMessage;
import com.ps.bingo.validator.NumberPerRowValidator;
import com.ps.bingo.validator.NumberRangeValidator;
import com.ps.bingo.validator.PlayerNumberValidator;
import com.ps.bingo.validator.ResultMessage;
import com.ps.bingo.validator.SettingValidator;
import com.ps.bingo.validator.TicketSizeValidator;
import com.ps.bingo.validator.ValidatorMessageBundle;

public class Simulator {
	static final Logger logger = LogManager.getLogger(Simulator.class.getName());

	private final InputStream cmdin;
	private final PrintStream cmdout;
	private final PrintStream cmderr;

	public Simulator() {
		this.cmdin = System.in;
		this.cmdout = System.out;
		this.cmderr = System.err;
	}

	private void run() throws BingoException, InterruptedException {
		cmdout.println(SimulatorMessageBundle.WELCOME.format());
		cmdout.println(SimulatorMessageBundle.WELCOME_NOTE.format());
		Scanner scanner = null;

		try {
			scanner = new Scanner(cmdin);
			GameSettings gameSettings = readGameSettings(scanner);
			UUID gameId = UUID.randomUUID();

			logger.debug(SimulatorMessageBundle.GAME_ID.format(gameId));
			logger.debug(gameSettings.toString());

			cmdout.println(SimulatorMessageBundle.NEXT_PROMPT.format());
			LockManager lockManager = new LockManager(gameSettings.getPlayerNumber());
			ErrorCallback errorCallback = new ErrorCallback(this);
			Game game = new Game(gameSettings).buildAndStart(lockManager, errorCallback, cmdout);
			while (!game.isDone()) {
				if (scanner.hasNextLine()) {
					String input = scanner.nextLine();
					maybeExit(input);
					if (Constants.NEXT_CMD.equals(input.toUpperCase())) {
						lockManager.resetNextCommandLatch(); // set latch to 1
						game.sendNextCommand();
						lockManager.nextCommandLatchAwait(); // wait for caller to count down
					}
				}
			}
			game.printResult(cmdout);

		} finally {
			if (scanner != null)
				scanner.close();
		}
	}

	private GameSettings readGameSettings(Scanner scanner) throws BingoException {
		GameSettings settings = new GameSettings();

		int numberRange = readSetting(scanner, new NumberRangeValidator(), SimulatorMessageBundle.NUMER_RANGE_FIELD,
				SimulatorMessageBundle.NUMER_RANGE_PROMPT, "90");
		settings.setNumberRange(numberRange);

		int playerNumber = readSetting(scanner, new PlayerNumberValidator(), SimulatorMessageBundle.PLAYER_NUMBER_FIELD,
				SimulatorMessageBundle.PLAYER_NUMBER_PROMPT, null);
		settings.setPlayerNumber(playerNumber);

		int[] ticketSize = readTicketSizeSetting(scanner, new TicketSizeValidator(numberRange),
				SimulatorMessageBundle.TICKET_SIZE_FIELD, SimulatorMessageBundle.TICKET_SIZE_PROMPT, "3X10");
		if (ticketSize.length < 2)
			throw new BingoException(SimulatorMessageBundle.INTERNAL_ERROR.format());
		settings.setTicketRowSize(ticketSize[0]);
		settings.setTicketColumnSize(ticketSize[1]);

		int numberPerRow = readSetting(scanner,
				new NumberPerRowValidator(settings.getTicketRowSize(), settings.getTicketColumnSize(),
						settings.getNumberRange()),
				SimulatorMessageBundle.NUMBER_PER_ROW_FIELD, SimulatorMessageBundle.NUMBER_PER_ROW_PROMPT, "5");
		settings.setNumberPerRow(numberPerRow);

		return settings;
	}

	private int readSetting(Scanner scanner, SettingValidator<String> validator, BundleMessage fieldNameMsg,
			BundleMessage promptMsg, String defaultValue) {
		int inputValue = -1;
		ResultMessage validateResult = new ResultMessage(false);
		do {
			cmdout.println(promptMsg.format());
			if (scanner.hasNextLine()) {
				String input = scanner.nextLine();
				maybeExit(input);
				if (input.equals("") && defaultValue != null)
					input = defaultValue;
				validateResult = validator.validate(input);
				if (!validateResult.isSuccess()) {
					cmderr.println(validateResult.getErrorMessage());
				} else
					inputValue = Integer.parseInt(input);
			}
		} while (!validateResult.isSuccess());
		return inputValue;
	}

	private int[] readTicketSizeSetting(Scanner scanner, SettingValidator<String> validator, BundleMessage fieldNameMsg,
			BundleMessage promptMsg, String defaultValue) {
		int rowSize = -1;
		int colSize = -1;
		boolean inputValid = true;
		do {
			cmdout.println(promptMsg.format());
			if (scanner.hasNextLine()) {
				String input = scanner.nextLine();
				maybeExit(input);
				if (input.equals("") && defaultValue != null)
					input = defaultValue;
				ResultMessage validateResult = validator.validate(input);
				inputValid = validateResult.isSuccess();
				if (validateResult.isSuccess()) {
					String[] rowsAndCols = input.split("X");
					if (rowsAndCols.length != 2) {
						inputValid = false;
					} else {
						try {
							rowSize = Integer.parseInt(rowsAndCols[0]);
							colSize = Integer.parseInt(rowsAndCols[1]);
						} catch (Exception e) {
							inputValid = false;
						}
					}
				}
				if (!inputValid) {
					cmderr.println(validateResult.getErrorMessage());
				}

			}
		} while (!inputValid);
		return new int[] { rowSize, colSize };

	}

	private void maybeExit(String cmd) {
		if (cmd != null && Constants.EXIT_CMD.equals(cmd.toUpperCase())) {
			cmdout.println(SimulatorMessageBundle.EXIT.format());
			System.exit(0);
		}
	}

	public void exitWithError(Throwable t) {
		cmderr.println(t.getMessage());
		cmderr.println(SimulatorMessageBundle.EXIT.format());
		logger.error(t);
		System.exit(-1);
	}

	public static void main(String[] args) {
		// Locale.setDefault(new Locale("fr", "FRANCE"));
		if (args.length >= 1) {
			Level level = null;
			boolean isConsoleEnabled = false;
			boolean isUpdated = false;
			try {
				for (String arg : args) {
					if (arg.trim().startsWith("--log=")) {
						level = Level.valueOf(arg.trim().substring(6));
						if (level != null)
							isUpdated = true;

					} else if (arg.trim().startsWith("--console=")) {
						String consoleEnabled = arg.trim().substring(10);
						if (consoleEnabled != null && consoleEnabled.trim().equalsIgnoreCase("ON")) {
							isConsoleEnabled = true;
						}
					}
				}
				if (isUpdated) {
					LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
					Configuration config = ctx.getConfiguration();
					LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
					if (level != null) {
						loggerConfig.setLevel(level);
					}
					if (isConsoleEnabled) {
						PatternLayout layout = PatternLayout.createDefaultLayout();
						ConsoleAppender console = ConsoleAppender.createDefaultAppenderForLayout(layout);
						console.start();
						loggerConfig.addAppender(console, (level == null ? Level.ERROR : level), null);
					}
					ctx.updateLoggers();
				}

			} catch (Exception e) {
				System.out.println(SimulatorMessageBundle.SET_LOG_LEVEL_ERROR.format() + e.getMessage());
				logger.error(SimulatorMessageBundle.SET_LOG_LEVEL_ERROR.formatWithErrorCode(), e);
			}
		}

		Simulator s = new Simulator();
		try {
			s.run();
		} catch (Exception e) {
			System.out.println(SimulatorMessageBundle.RUN_SIMULATOR_ERROR.format() + e.getMessage());
			logger.error(SimulatorMessageBundle.RUN_SIMULATOR_ERROR.formatWithErrorCode(), e);
		}
	}
}
