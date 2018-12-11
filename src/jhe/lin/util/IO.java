package jhe.lin.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;

public class IO {

	public static float[][] readExcel_distance(int size) {
		File excelFile = new File("distance.xls");
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
