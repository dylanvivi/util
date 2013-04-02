package com.dylan.lang;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lucas ^_^
 * @date 2012-12-11
 * @Description 
 */
public class ExcelUtil {

	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

	/**  
	* 功能：生成Excel文件  
	* @param wb	HSSFWorkbook  
	* @param fileName 写入文件的相对路径 （包含文件名） 
	*/
	public static void writeWorkbook(HSSFWorkbook wb, String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	public static <T> void exportExcel(String title, String[] headers, String[] cols, Collection<T> dataset,
			OutputStream out, String pattern, String[] sumcols) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = createStyle(workbook, HSSFColor.LIME.index, HSSFCellStyle.SOLID_FOREGROUND,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		//生成一个字体
		HSSFFont font = createFont(workbook, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("微软雅黑");
		//字体设置给样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = createStyle(workbook, HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		//生成数字类型样式(三位小数)
		HSSFCellStyle style3 = createStyle(workbook, HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFDataFormat format = workbook.createDataFormat();
		style3.setDataFormat(format.getFormat("0.000"));

		//生成数字类型样式(整数)
		HSSFCellStyle style4 = createStyle(workbook, HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("happy life"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("dylan");

		// 产生表格标题行
		int index = 0;
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		index++;
		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		while (it.hasNext()) {
			row = sheet.createRow(index);
			index++;
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				for (int j = 0; j < cols.length; j++) {
					boolean hasField = false;
					if (cols[j].equalsIgnoreCase(fieldName)) {
						hasField = true;
					}
					if (!hasField) {
						continue;
					}
					HSSFCell cell = row.createCell(j);
					cell.setCellStyle(style2);
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					try {
						Class tCls = t.getClass();
						Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
						Object value = getMethod.invoke(t, new Object[] {});
						// 判断值的类型后进行强制类型转换
						String textValue = null;
						if (value instanceof Double || value instanceof Float) {
							textValue = value.toString();
						} else if (value instanceof Date) {
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else {
							// 其它数据类型都当作字符串简单处理
							if (value == null) {
								textValue = null;
							} else {
								textValue = value.toString();
							}

						}
						// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
						if (textValue != null) {
							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
							Matcher matcher = p.matcher(textValue);
							if (matcher.matches()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								cell.setCellValue(textValue);
							}
						}
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} finally {
						// 清理资源
					}
				}

			}

		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据map输出excel结果集
	 * @param title - sheet的名称
	 * @param headers - 列名
	 * @param cols - 要输出的列名(无视大小写)
	 * @param dataset - 输出对象Map
	 * @param out - 输出流
	 * @param pattern - 日期格式
	 * @param sumcols - 合计行, 没有则传null
	 */
	public static void export(String title, String[] headers, String[] cols, Collection<Map<String, Object>> dataset,
			OutputStream out, String pattern, String[] sumcols) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = createStyle(workbook, HSSFColor.LIME.index, HSSFCellStyle.SOLID_FOREGROUND,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		//生成一个字体
		HSSFFont font = createFont(workbook, HSSFColor.BLACK.index, HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("微软雅黑");
		//字体设置给样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = createStyle(workbook, HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		//生成数字类型样式(三位小数)
		HSSFCellStyle style3 = createStyle(workbook, HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFDataFormat format = workbook.createDataFormat();
		style3.setDataFormat(format.getFormat("0.000"));

		//生成数字类型样式(整数)
		HSSFCellStyle style4 = createStyle(workbook, HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN,
				HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style4.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("happy life"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("dylan");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<Map<String, Object>> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Map<String, Object> t = (Map<String, Object>) it.next();
			Object[] keys = t.keySet().toArray();
			for (int i = 0; i < cols.length; i++) {
				String fieldName = "";
				String col = cols[i];
				boolean hasCol = false;
				for (Object fieldNameStr : keys) {
					if (col.equalsIgnoreCase(fieldNameStr.toString())) {
						hasCol = true;
						fieldName = fieldNameStr.toString();
						break;
					}
				}
				if (!hasCol) {
					continue;
				}
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);

				try {
					Object value = t.get(fieldName);
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Double || value instanceof Float) {
						String fvalue = value.toString();
						textValue = StringUtils.trimFloat(fvalue, 2);
					} else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
								index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						if (value == null) {
							textValue = null;
						} else {
							textValue = value.toString();
						}

					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+\\.\\d+?$");
						Matcher matcher = p.matcher(textValue);
						Pattern pn = Pattern.compile("^\\d+$");
						Matcher matcherPN = pn.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellStyle(style3);
							cell.setCellValue(Double.parseDouble(textValue));
						} else if (matcherPN.matches()) {
							cell.setCellStyle(style4);
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							cell.setCellValue(textValue);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}

		}
		//增加合计行
		if (sumcols != null) {
			row = sheet.createRow(index + 1);
			for (int i = 0; i < sumcols.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				HSSFFont font4 = workbook.createFont();
				font4.setColor(HSSFColor.BLACK.index);
				font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				style2.setFont(font4);
				String formula = null;
				if (i == 0) {
					HSSFRichTextString richString = new HSSFRichTextString("合计");

					richString.applyFont(font4);
					cell.setCellValue(richString);

				} else if ("0".equals(sumcols[i])) { //不计算
					cell.setCellValue(new HSSFRichTextString(""));
				} else {
					formula = "SUM(" + sumcols[i] + "2:" + sumcols[i] + (index + 1) + ")";
					cell.setCellFormula(formula);
				}
			}
		}

		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static HSSFCellStyle createStyle(HSSFWorkbook workbook, short fillForegroundColor, short fillPattern,
			short borderBottom, short borderleft, short borderright, short bordertop, short alignment) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(fillForegroundColor);
		style.setFillPattern(fillPattern);
		style.setBorderBottom(borderBottom);
		style.setBorderLeft(borderleft);
		style.setBorderRight(borderright);
		style.setBorderTop(bordertop);
		style.setAlignment(alignment);
		return style;
	}

	private static HSSFFont createFont(HSSFWorkbook workbook, short color, short bold) {
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(color);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(bold);
		return font;
	}
}
