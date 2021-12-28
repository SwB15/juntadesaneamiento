package Controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author SwichBlade15
 */
public class ExportarExcel {

    HSSFRow fila;

    public void exportarExcel(JTable t) throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar archivo");
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().toString().concat(".xls"); // Extencion del archivo de excel"
            try {
                File archivoXLS = new File(ruta);
                if (archivoXLS.exists()) {
                    archivoXLS.delete();
                }
                archivoXLS.createNewFile();
                HSSFWorkbook libro = new HSSFWorkbook();

                try (FileOutputStream archivo = new FileOutputStream(archivoXLS)) {
                    HSSFSheet hoja = libro.createSheet("Datos"); // Nombre de la hoja d calculo hoja.setOisplayGridlines(false);"
                    for (int f = 0; f < t.getRowCount(); f++) {
                        fila = hoja.createRow(f);
                        for (int c = 0; c < t.getColumnCount(); c++) {
                            HSSFCell celda = fila.createCell(c);
                            if (f == 0) {
                                // Aunque no es necesario podemos establecer estilos a las celdas.
                                // Primero, establecemos el tipo de fuente
                                HSSFFont fuente = libro.createFont();
                                fuente.setFontHeightInPoints((short) 12);
                                fuente.setFontName(HSSFFont.FONT_ARIAL);
                                fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                                // Luego creamos el objeto que se encargará de aplicar el estilo a la celda
                                HSSFCellStyle estiloCelda = libro.createCellStyle();
                                estiloCelda.setWrapText(true);
                                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                                estiloCelda.setFont(fuente);

                                // También, podemos establecer bordes...
                                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setBottomBorderColor((short) 8);
                                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setLeftBorderColor((short) 8);
                                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setRightBorderColor((short) 8);
                                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setTopBorderColor((short) 8);

                                celda.setCellStyle(estiloCelda);
                                celda.setCellType(HSSFCell.CELL_TYPE_STRING);
                                // Finalmente, establecemos el valor
                                celda.setCellValue(t.getColumnName(c));
                            }
                        }
                    }

                    int filalnicio = 1;
                    for (int f = 0; f < t.getRowCount(); f++) {
                        fila = hoja.createRow(filalnicio);
                        filalnicio++;
                        for (int c = 0; c < t.getColumnCount(); c++) {
                            HSSFCell celda = fila.createCell(c);
                            if (t.getValueAt(f, c) instanceof Double) {
                                // Aunque no es necesario podemos establecer estilos a las celdas.
                                // Primero, establecemos el tipo de fuente
                                HSSFFont fuente = libro.createFont();
                                fuente.setFontHeightInPoints((short) 11);
                                fuente.setFontName(HSSFFont.FONT_ARIAL);

                                // Luego creamos el objeto que se encargará de aplicar el estilo a la celda
                                HSSFCellStyle estiloCelda = libro.createCellStyle();
                                estiloCelda.setWrapText(true);
                                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                                estiloCelda.setFont(fuente);

                                // También, podemos establecer bordes...
                                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setBottomBorderColor((short) 8);
                                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setLeftBorderColor((short) 8);
                                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setRightBorderColor((short) 8);
                                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setTopBorderColor((short) 8);

                                celda.setCellStyle(estiloCelda);
                                celda.setCellType(HSSFCell.CELL_TYPE_STRING);

                                // Finalmente, establecemos el valor
                                celda.setCellValue(Double.parseDouble(t.getValueAt(f, c).toString()));
                            } else if (t.getValueAt(f, c) instanceof Float) {
                                // Aunque no es necesario podemos establecer estilos a las celdas.
                                // Primero, establecemos el tipo de fuente
                                HSSFFont fuente = libro.createFont();
                                fuente.setFontHeightInPoints((short) 11);
                                fuente.setFontName(HSSFFont.FONT_ARIAL);

                                // Luego creamos el objeto que se encargará de aplicar el estilo a la celda
                                HSSFCellStyle estiloCelda = libro.createCellStyle();
                                estiloCelda.setWrapText(true);
                                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                                estiloCelda.setFont(fuente);

                                // También, podemos establecer bordes...
                                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setBottomBorderColor((short) 8);
                                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setLeftBorderColor((short) 8);
                                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setRightBorderColor((short) 8);
                                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setTopBorderColor((short) 8);

                                celda.setCellStyle(estiloCelda);
                                celda.setCellType(HSSFCell.CELL_TYPE_STRING);

                                // Finalmente, establecemos el valor
                                celda.setCellValue(Float.parseFloat((String) t.getValueAt(f, c)));
                            } else {
                                // Aunque no es necesario podemos establecer estilos a las celdas.
                                // Primero, establecemos el tipo de fuente
                                HSSFFont fuente = libro.createFont();
                                fuente.setFontHeightInPoints((short) 11);
                                fuente.setFontName(HSSFFont.FONT_ARIAL);

                                // Luego creamos el objeto que se encargará de aplicar el estilo a la celda
                                HSSFCellStyle estiloCelda = libro.createCellStyle();
                                estiloCelda.setWrapText(true);
                                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                                estiloCelda.setFont(fuente);

                                // También, podemos establecer bordes...
                                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DASHED);
                                estiloCelda.setBottomBorderColor((short) 8);
                                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setLeftBorderColor((short) 8);
                                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setRightBorderColor((short) 8);
                                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
                                estiloCelda.setTopBorderColor((short) 8);

                                celda.setCellStyle(estiloCelda);
                                celda.setCellType(HSSFCell.CELL_TYPE_STRING);

                                // Finalmente, establecemos el valor
                                celda.setCellValue(String.valueOf(t.getValueAt(f, c)));
                            }
                        }
                        
                        for (int i = 0; i < 6; i++) {
                            hoja.autoSizeColumn(i);
                        }
                    }

                    libro.write(archivo);
                } // Nombre de la hoja d calculo hoja.setOisplayGridlines(false);"
                Desktop.getDesktop().open(archivoXLS);
            } catch (IOException | NumberFormatException ex) {
                throw ex;
            }
        }
    }
}
