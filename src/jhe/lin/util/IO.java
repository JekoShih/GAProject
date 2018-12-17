package jhe.lin.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;

public class IO {

	@SuppressWarnings("resource")
	public static float[][] readExcel(int size, String fileName) {
		File excelFile = new File(fileName);
		float d[][] = new float[size][size];
		try {
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
			HSSFSheet sheet = wb.getSheetAt(0);
			int i = 0;
			for (Row row : sheet) {
				int j = 0;
				for (Cell cell : row) {
					d[i][j] = (float) cell.getNumericCellValue();
					j++;
					if (j == size)
						break;
				}
				i++;
				if (i == size)
					return d;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}
}
