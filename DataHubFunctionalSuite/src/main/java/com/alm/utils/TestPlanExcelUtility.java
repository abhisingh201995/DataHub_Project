package com.alm.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestPlanExcelUtility {
	private static Workbook workbook;
	private static Sheet sheet;
	
	public static void generateReport(Map<String, List<Integer>> headlinesTestCaseCount) throws IOException {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("TestPlanData");

		createHeaderRow();
	
		CellStyle headlineCellStyle = getHeadlineCellStyle();
		CellStyle cellStyle = getDataCellStyle();	
		CellStyle footerCellStyle = getDataFooterCellStyle();
		Set<String> keyset = headlinesTestCaseCount.keySet();
		int rownum = 2;
		int startIndex = rownum+1;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			List<Integer> objArr = headlinesTestCaseCount.get(key);
			int cellnum = 0;
			Cell cell = row.createCell(cellnum++);
			cell.setCellValue(key);
			cell.setCellStyle(headlineCellStyle);
			for (int obj : objArr) {
				cell = row.createCell(cellnum++);
				cell.setCellValue(obj);
				cell.setCellStyle(cellStyle);
			}
			cell = row.createCell(cellnum++);			
			cell.setCellValue(ALMConstant.TestPlanFolder.getDomainForKey(key));
			cell.setCellStyle(cellStyle);
			cell = row.createCell(18);
			String formula= "B"+rownum+"-F"+rownum;
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellFormula(formula);
			cell.setCellStyle(cellStyle);
		}
		int endIndex = rownum;

		Row row = sheet.createRow(rownum++);
		char[] alpha = new char[26];		   
		
		for (int i = 1; i <= 16; i++) {
			alpha[i] = (char)(65 + i);
			Cell cell = row.createCell(i);
			String formula= "SUM("+alpha[i]+startIndex+":"+alpha[i]+endIndex+")";
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellFormula(formula);
			cell.setCellStyle(footerCellStyle);			
		}

		writeExcel(CommonParameters.EXCEL_REPORT_PATH);	

	}
	
	private static CellStyle getHeaderCellStyle(IndexedColors color) {
		Font headerFont = workbook.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 12);

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle = updateCellStyleWithBorder(headerCellStyle);
		headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(color.getIndex());
		headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		return headerCellStyle;
	}
	
	private static CellStyle getHeadlineCellStyle() {
		Font headlineFont = workbook.createFont();
		headlineFont.setBoldweight((short) 4);
		headlineFont.setFontHeightInPoints((short) 12);
		CellStyle headlineCellStyle = workbook.createCellStyle();
		headlineCellStyle = updateCellStyleWithBorder(headlineCellStyle);
		headlineCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		headlineCellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		headlineCellStyle.setFont(headlineFont);
		headlineCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
		headlineCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return headlineCellStyle;
	}
	private static CellStyle getDataCellStyle() {
		Font dataFont = workbook.createFont();
		dataFont.setBoldweight((short) 2);
		dataFont.setFontHeightInPoints((short) 12);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle = updateCellStyleWithBorder(cellStyle);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setFont(dataFont);
		return cellStyle;
	}
	
	private static CellStyle getDataFooterCellStyle() {
		Font footerFont = workbook.createFont();
		footerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		footerFont.setFontHeightInPoints((short) 12);
		CellStyle footerCellStyle = workbook.createCellStyle();
		footerCellStyle = updateCellStyleWithBorder(footerCellStyle);
		footerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		footerCellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		footerCellStyle.setFont(footerFont);
		return footerCellStyle;
	}
	
	private static CellStyle updateCellStyleWithBorder(CellStyle cellStyle) {
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		return cellStyle;
	}
	
	private static void createHeaderRow() {	
		
		CellStyle headerCellStyle_0 = getHeaderCellStyle(IndexedColors.GREY_25_PERCENT);
		CellStyle headerCellStyle_1 = getHeaderCellStyle(IndexedColors.CORAL);
		CellStyle headerCellStyle_2 = getHeaderCellStyle(IndexedColors.LIGHT_GREEN);
		CellStyle headerCellStyle_3 = getHeaderCellStyle(IndexedColors.LAVENDER);
		CellStyle headerCellStyle_4 = getHeaderCellStyle(IndexedColors.LEMON_CHIFFON);
		CellStyle headerCellStyle_5 = getHeaderCellStyle(IndexedColors.PALE_BLUE);
		
		Row topHeaderRow = sheet.createRow(0);
		
		Cell cell = topHeaderRow.createCell(0);
		cell.setCellValue("Headlines");
		cell.setCellStyle(headerCellStyle_0);
		
		cell = topHeaderRow.createCell(1);
		cell.setCellValue("Total Test Cases");
		cell.setCellStyle(headerCellStyle_1);
		
		cell = topHeaderRow.createCell(5);
		cell.setCellValue("Regression");
		cell.setCellStyle(headerCellStyle_2);
		
		cell = topHeaderRow.createCell(9);
		cell.setCellValue("Automated");
		cell.setCellStyle(headerCellStyle_3);
		
		cell = topHeaderRow.createCell(13);
		cell.setCellValue("To Be Automated");
		cell.setCellStyle(headerCellStyle_4);
		
		cell = topHeaderRow.createCell(17);
		cell.setCellValue("Domain");
		cell.setCellStyle(headerCellStyle_5);
		
		cell = topHeaderRow.createCell(18);
		cell.setCellValue("Gaps in total");
		cell.setCellStyle(headerCellStyle_5);
		
		Row headerRow = sheet.createRow(1);

		cell = headerRow.createCell(1);
		cell.setCellValue("Total");
		cell.setCellStyle(headerCellStyle_1);

		cell = headerRow.createCell(2);
		cell.setCellValue("UI");
		cell.setCellStyle(headerCellStyle_1);

		cell = headerRow.createCell(3);
		cell.setCellValue("API");
		cell.setCellStyle(headerCellStyle_1);

		cell = headerRow.createCell(4);
		cell.setCellValue("Others");
		cell.setCellStyle(headerCellStyle_1);

		cell = headerRow.createCell(5);
		cell.setCellValue("Total");
		cell.setCellStyle(headerCellStyle_2);

		cell = headerRow.createCell(6);
		cell.setCellValue("UI");
		cell.setCellStyle(headerCellStyle_2);

		cell = headerRow.createCell(7);
		cell.setCellValue("API");
		cell.setCellStyle(headerCellStyle_2);

		cell = headerRow.createCell(8);
		cell.setCellValue("Others");
		cell.setCellStyle(headerCellStyle_2);
		
		cell = headerRow.createCell(9);
		cell.setCellValue("Total");
		cell.setCellStyle(headerCellStyle_3);

		cell = headerRow.createCell(10);
		cell.setCellValue("UI");
		cell.setCellStyle(headerCellStyle_3);

		cell = headerRow.createCell(11);
		cell.setCellValue("API");
		cell.setCellStyle(headerCellStyle_3);

		cell = headerRow.createCell(12);
		cell.setCellValue("Others");
		cell.setCellStyle(headerCellStyle_3);
		
		cell = headerRow.createCell(13);
		cell.setCellValue("Total");
		cell.setCellStyle(headerCellStyle_4);

		cell = headerRow.createCell(14);
		cell.setCellValue("UI");
		cell.setCellStyle(headerCellStyle_4);

		cell = headerRow.createCell(15);
		cell.setCellValue("API");
		cell.setCellStyle(headerCellStyle_4);

		cell = headerRow.createCell(16);
		cell.setCellValue("Others");
		cell.setCellStyle(headerCellStyle_4);
		
		cell = headerRow.createCell(17);
		cell.setCellStyle(headerCellStyle_5);
		
		cell = headerRow.createCell(18);
		cell.setCellStyle(headerCellStyle_5);
	}

	private static void writeExcel(String folderPath) throws IOException {		
		sheet.setDisplayGridlines(false);
		sheet.autoSizeColumn(0);
		for (int i = 1; i <= 16; i++) {
			sheet.setColumnWidth(i, 50*40);
		}
		sheet.autoSizeColumn(17);
		sheet.autoSizeColumn(18);
		
		sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,4));
		sheet.addMergedRegion(new CellRangeAddress(0,0,5,8));
		sheet.addMergedRegion(new CellRangeAddress(0,0,9,12));
		sheet.addMergedRegion(new CellRangeAddress(0,0,13,16));
		sheet.addMergedRegion(new CellRangeAddress(0,1,17,17));
		sheet.addMergedRegion(new CellRangeAddress(0,1,18,18));
		
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
		
	}

}
