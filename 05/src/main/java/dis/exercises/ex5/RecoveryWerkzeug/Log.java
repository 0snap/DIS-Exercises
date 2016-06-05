package RecoveryWerkzeug;

import java.util.List;
import java.util.logging.LogRecord;

/**
 * Transaction log of the recovery manager.
 * Abstraction to work with the LogRecord
 * 
 */

public class Log
{

	private List<LogRecord> records;

	public List<LogRecord> getRecords() {
		return records;
	}

	public Log(List<LogRecord> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (LogRecord record : records) {
			stringBuilder.append(record.toString() + "\n");
		}

		return stringBuilder.toString();
	}
}
