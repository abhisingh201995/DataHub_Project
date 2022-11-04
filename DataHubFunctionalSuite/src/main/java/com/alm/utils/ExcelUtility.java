package com.alm.utils;

import com.alm.utils.ALMConstant.HeadlineDomainMapping;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class ExcelUtility {

	private static Workbook workbook;
	private static Sheet sheet;
	private static int rowCounter = 5;
	private static int TOTAL_FAILED_COUNT = 0;
	private static int TOTAL_PASS_COUNT = 0;
	private static int TOTAL_SKIP_COUNT = 0;
	static Properties uiProp = new Properties();
	static Map<String,String> reportLinkKey = new HashMap<String,String>();

	public static void generateResultReport(String testCaseType, String folderPath,
			Map<String, Integer> failedTestCases, Map<String, Integer> passedTestCases,
			Map<String, Integer> skippedTestCases, Map<String, Integer> totalALMTestCaseCount)throws Exception {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet(testCaseType);
		createHeadLine(testCaseType);
		CellStyle headerCellStyle = getHeaderCellStyle();
		createHeaderRow(headerCellStyle,testCaseType);
		iterateOverTestCaseCount(failedTestCases, passedTestCases, skippedTestCases,totalALMTestCaseCount,testCaseType);
		updateTotalCountRow();
		writeExcel(folderPath,testCaseType);
		
	}

	private static void createHeadLine(String testCaseType) {
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		CellStyle cellStyle = workbook.createCellStyle();
		Font dataFont = workbook.createFont();
		dataFont.setBoldweight((short) 3);
		dataFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		dataFont.setFontHeightInPoints((short) 14);
		cellStyle = updateCellStyleWithBorder(cellStyle);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyle.setFont(dataFont);
		cell.setCellValue(testCaseType +" Execution Report");
		cell.setCellStyle(cellStyle);

	}

	private static void updateTotalCountRow() {
		Row row = sheet.createRow(1);
		Cell cell = row.createCell(0);
		CellStyle headerStyle = getHeaderCellStyle();
		cell.setCellValue("TOTAL");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(1);
		cell.setCellValue("Passed");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(2);
		cell.setCellValue("Failed");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(3);
		cell.setCellValue("Skipped");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(4);
		cell.setCellValue("Pass Percentage");
		cell.setCellStyle(headerStyle);

		row = sheet.createRow(2);
		CellStyle cellStyle = getDataCellStyle();
		cell = row.createCell(0);
		int total = TOTAL_FAILED_COUNT + TOTAL_PASS_COUNT + TOTAL_SKIP_COUNT;
		cell.setCellValue(total);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(1);
		cell.setCellValue(TOTAL_PASS_COUNT);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(2);
		cell.setCellValue(TOTAL_FAILED_COUNT);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(3);
		cell.setCellValue(TOTAL_SKIP_COUNT);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(4);
		double percentage = 0;
		String percentage1 = "";
		if(TOTAL_PASS_COUNT !=0) {
			percentage =  (TOTAL_PASS_COUNT*100.00)/total ;
		}else {
			percentage = 0;
		}
		DecimalFormat df = new DecimalFormat("###.##");
		
		percentage1 = df.format(percentage) + "%";
		cell.setCellValue(percentage1);
		cell.setCellStyle(cellStyle);

	}

	private static void iterateOverTestCaseCount(Map<String, Integer> failedTestCases,
			Map<String, Integer> passedTestCases, Map<String, Integer> skippedTestCases, Map<String, Integer> totalALMTestCaseCount, String testCaseType) throws FileNotFoundException, IOException {
		Set<String> headlines = failedTestCases.keySet();
		List<String> headlineList = Utils.getHeadlineList();
		if (testCaseType.equals("UI")) {
			InputStream inputStream = new FileInputStream(
					new File(System.getProperty("user.dir") + "\\Platform_UI_Jobs_Link.properties"));
			uiProp.load(inputStream);
		} else {
			InputStream inputStream = new FileInputStream(
					new File(System.getProperty("user.dir") + "\\Platform_API_Jobs_Link.properties"));
			uiProp.load(inputStream);
		}
		loadReportLinkMapping();
		for(HeadlineDomainMapping headlinedomainHeadline : HeadlineDomainMapping.values()){
			String domain = headlinedomainHeadline.getDomain();
			int domainFailCount = 0;
			int domainPassCount = 0;
			int domainSkipCount = 0;
			int initialMergedRow = rowCounter;
			List<String> headlineDomainList = headlinedomainHeadline.getHeadlineForDomain(domain);
			for(String headlineDomain : headlineDomainList) {	
				if(headlines.contains(headlineDomain) && headlineList.contains(headlineDomain)) {
					int failedCount = failedTestCases.get(headlineDomain);
					TOTAL_FAILED_COUNT = TOTAL_FAILED_COUNT + failedCount;
					domainFailCount = domainFailCount + failedCount;
					int passCount = passedTestCases.get(headlineDomain);
					TOTAL_PASS_COUNT = TOTAL_PASS_COUNT + passCount;
					domainPassCount = domainPassCount + passCount;
					int skipCount = skippedTestCases.get(headlineDomain);
					TOTAL_SKIP_COUNT = TOTAL_SKIP_COUNT + skipCount;
					domainSkipCount = domainSkipCount + skipCount;
					updateExcelSheet(headlineDomain, failedCount, passCount, skipCount,totalALMTestCaseCount,testCaseType);	
				}

			}
			updateDomainTotalAndMerge(initialMergedRow,domain,domainFailCount,domainPassCount,domainSkipCount);
		}
		uiProp.clear();
	}

	private static void updateDomainTotalAndMerge(int initialMergedRow, String domain, int domainFailCount,
			int domainPassCount, int domainSkipCount) {
		// TODO Auto-generated method stub
		CellStyle cellStyle = getDataCellStyle();
		Row row = sheet.getRow(initialMergedRow);
		Row row1 = sheet.createRow(rowCounter);
		Cell cell = row.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(domain);
		
		
		cell = row1.createCell(1);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("Total for "+domain);
		
		int total = domainFailCount + domainPassCount + domainSkipCount;
		
		cell = row1.createCell(2);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(total);
		
		cell = row1.createCell(3);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(domainPassCount);
		
		cell = row1.createCell(4);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(domainFailCount);
		
		cell = row1.createCell(5);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(domainSkipCount);
		
		cell = row1.createCell(6);
		cell.setCellStyle(cellStyle);
		double percentage = 0;
		if (domainPassCount != 0) {
			percentage = (domainPassCount * 100.00) / total;
		}else {
			percentage = 0;
		}
		DecimalFormat df = new DecimalFormat("###.##");
		
		String percentage1 = df.format(percentage) + "%";
		cell.setCellValue(percentage1);
		sheet.addMergedRegion(new CellRangeAddress(initialMergedRow, rowCounter, 0, 0));
		rowCounter++;
	}

	private static void loadReportLinkMapping() {
		Iterator<Object> iterator = uiProp.keySet().iterator();
		while(iterator.hasNext()) {
			String key = (String) iterator.next();
			if(key.contains("Platform_Services_Regression")) {
				reportLinkKey.put("Platform_Services_Regression", key);
			}else if(key.contains("HCA_And_KPI_Regression")) {
				reportLinkKey.put("HCA_And_KPI_Regression", key);
			}else if(key.contains("HCM_And_Integration_Regression")) {
				reportLinkKey.put("HCM_And_Integration_Regression", key);
			}
			
		}
		
		
	}

	private static void updateExcelSheet(String headlineName, int failedCount, int passCount, int skipCount, Map<String, Integer> totalALMTestCaseCount, String testCaseType) {
		
		Row row = sheet.createRow(rowCounter);
		CellStyle cellStyle = getDataCellStyle();
		int dataCellCounter = 1;
		Cell cell = row.createCell(dataCellCounter++);
		cell.setCellValue(headlineName);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(dataCellCounter++);
		int total = passCount + failedCount + skipCount;
		cell.setCellValue(total);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(dataCellCounter++);
		cell.setCellValue(passCount);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(dataCellCounter++);
		cell.setCellValue(failedCount);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(dataCellCounter++);
		cell.setCellValue(skipCount);
		cell.setCellStyle(cellStyle);
		cell = row.createCell(dataCellCounter++);
		double percentage = 0;
		if (passCount != 0) {
			percentage = (passCount * 100.00) / total;
		}else {
			percentage = 0;
		}
		DecimalFormat df = new DecimalFormat("###.##");
		
		String percentage1 = df.format(percentage) + "%";
		
		cell.setCellValue(percentage1);
		cell.setCellStyle(cellStyle);

		cell = row.createCell(dataCellCounter++);
		cell.setCellStyle(getHyperLinkStyle());
		
		if (testCaseType.equals("UI")) {
			String reportKey = ALMConstant.getUIJobLinks.getReportKeyByHeadline(headlineName).getReportName();
			if (reportKey != null) {
				if (reportLinkKey.get(reportKey) != null) {
					String link = uiProp.getProperty(reportLinkKey.get(reportKey));
					if (link != null) {
						if (!link.trim().isEmpty()) {
							cell.setCellValue(link.trim());
						}
					}
				}
			}

		} else {
			
			String reportKey = ALMConstant.getAPIJobLinks.getReportKeyByHeadline(headlineName).getReportName();
			if (reportKey != null) {
				if (reportLinkKey.get(reportKey) != null) {
					String link = uiProp.getProperty(reportLinkKey.get(reportKey));
					if (link != null) {
						if (!link.trim().isEmpty()) {
							cell.setCellValue(link.trim());
						}
					}
				}
			}
		}
		
		int testPlanCount = totalALMTestCaseCount.get(headlineName);
		cell = row.createCell(dataCellCounter++);
		cell.setCellValue(testPlanCount);
		cell.setCellStyle(cellStyle);
		
		cell = row.createCell(dataCellCounter++);
		cell.setCellValue(testPlanCount - total);
		cell.setCellStyle(cellStyle);
		
		rowCounter++;

	}

	private static CellStyle getDataCellStyle() {
		Font dataFont = workbook.createFont();
		dataFont.setBoldweight((short) 1);
		dataFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle = updateCellStyleWithBorder(cellStyle);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		cellStyle.setFont(dataFont);
		return cellStyle;
	}

	private static CellStyle getHyperLinkStyle() {
		CellStyle hlinkstyle = workbook.createCellStyle();
		Font hlinkfont = workbook.createFont();
		hlinkfont.setUnderline(Font.U_SINGLE);
		//hlinkfont.setColor(IndexedColors.BLUE.index);
		hlinkfont.setFontHeightInPoints((short) 9);
		hlinkstyle = updateCellStyleWithBorder(hlinkstyle);
		hlinkstyle.setAlignment(CellStyle.ALIGN_LEFT);
		hlinkstyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		hlinkstyle.setWrapText(true);
		hlinkstyle.setFont(hlinkfont);
		return hlinkstyle;
	}

	private static CellStyle getHeaderCellStyle() {
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 12);

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle = updateCellStyleWithBorder(headerCellStyle);
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
		headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		return headerCellStyle;
	}

	private static CellStyle updateCellStyleWithBorder(CellStyle cellStyle) {
		cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		return cellStyle;
	}

	private static void createHeaderRow(CellStyle headerCellStyle, String testCaseType) {
		Row headerRow = sheet.createRow(4);
		int headerRowCounter = 0;
		Cell cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Domain");
		cell.setCellStyle(headerCellStyle);
		
		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Headline");
		cell.setCellStyle(headerCellStyle);

		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Total Count");
		cell.setCellStyle(headerCellStyle);

		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Passed");
		cell.setCellStyle(headerCellStyle);

		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Failed");
		cell.setCellStyle(headerCellStyle);

		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Skipped");
		cell.setCellStyle(headerCellStyle);

		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Pass Percentage");
		cell.setCellStyle(headerCellStyle);

		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Report Link");
		cell.setCellStyle(headerCellStyle);	
		
		
		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("From Test Plan");
		cell.setCellStyle(headerCellStyle);
		
		cell = headerRow.createCell(headerRowCounter++);
		cell.setCellValue("Gap in Execution");
		cell.setCellStyle(headerCellStyle);
	}

	private static void writeExcel(String folderPath, String testCaseType) throws  Exception {
		sheet.setDisplayGridlines(false);
		for (int i = 0; i < 7; i++) {
			sheet.autoSizeColumn(i);
		}
		sheet.setColumnWidth(6, 100*100);
		sheet.setColumnWidth(7, 100*100);
		File file = new File(folderPath);
		file.mkdirs();
		String reportFileName = "";

		if(folderPath.contains("\\")) {
			if (folderPath.endsWith("\\")) {
				reportFileName = folderPath + "ALMReport.xlsx";
			} else {
				reportFileName = folderPath + "\\ALMReport.xlsx";
			}
		}else {
			if (folderPath.endsWith("/")) {
				reportFileName = folderPath + "ALMReport.xlsx";
			} else {
				reportFileName = folderPath + "/ALMReport.xlsx";
			}	
		}
		
		FileOutputStream fileOut = new FileOutputStream(reportFileName);
		workbook.write(fileOut);
		fileOut.close();
		rowCounter = 1;
		TOTAL_FAILED_COUNT = 0;
		TOTAL_PASS_COUNT = 0;
		TOTAL_SKIP_COUNT = 0;
	}

	
}
