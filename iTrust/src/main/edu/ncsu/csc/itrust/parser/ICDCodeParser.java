package edu.ncsu.csc.itrust.parser;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import edu.ncsu.csc.itrust.model.icdcode.ICDCode;

public class ICDCodeParser {
	private static final int LIMIT = 1000;
	private static final int NAME_LENGTH = 30;
	private static final String INPUT_PATH = "src/main/edu/ncsu/csc/itrust/parser/cci_icd10cm_2017.csv";
	private static final String OUTPUT_PATH = "sql/data/icdcode.sql";

	private static ICDCode processLine(String[] tokens) {
		return new ICDCode(tokens[0], tokens[1].substring(0, Math.min(tokens[1].length(), NAME_LENGTH)), tokens[2].equals("1"));
	}

	private static String convertToSql(ICDCode code) {
		return String.format("('%s', '%s', %d)", code.getCode(), code.getName(), code.isChronic() ? 1 : 0);
	}

	public static void main(String[] args) throws Exception {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_PATH))) {
			writer.write("INSERT INTO icdcode "
					+ "(code, name, is_chronic) VALUES\n");
			String valuesSql = Files.lines(Paths.get(INPUT_PATH), Charset.forName("Cp1252")).skip(1).limit(LIMIT)
					.map(line -> line.replaceAll("\"|'", "").split(",")).map(ICDCodeParser::processLine).map(ICDCodeParser::convertToSql)
					.collect(Collectors.joining(",\n"));
			writer.write(valuesSql);
			writer.write("\nON duplicate key update code=code;");
		}
	}
}
