package edu.ncsu.csc.itrust.parser;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;

import edu.ncsu.csc.itrust.model.ndcode.NDCCode;

public class NDCCodeParser {
	private static final int LIMIT = 1000;
	private static final int DESCRIPTION_LIMIT = 100;
	private static final String INPUT_PATH = "src/main/edu/ncsu/csc/itrust/parser/package.csv";
	private static final String OUTPUT_PATH = "sql/data/ndcodes1000.sql";

	private static NDCCode processLine(String[] tokens) {
		String description = StringEscapeUtils.escapeSql(tokens[3]);
		return new NDCCode(tokens[1], description.substring(0, Math.min(description.length(), DESCRIPTION_LIMIT)));
	}

	private static String convertToSql(NDCCode code) {
		return String.format("('%s', '%s')", code.getCode(), code.getDescription());
	}

	public static void main(String[] args) throws Exception {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_PATH))) {
			writer.write("INSERT INTO ndcodes "
					+ "(code, description) VALUES\n");
			String valuesSql = Files.lines(Paths.get(INPUT_PATH), Charset.forName("Cp1252")).skip(1).limit(LIMIT)
					.map(line -> line.split("\t")).map(NDCCodeParser::processLine).map(NDCCodeParser::convertToSql)
					.collect(Collectors.joining(",\n"));
			writer.write(valuesSql);
			writer.write("\nON duplicate key update code=code;");
		}
	}
}
