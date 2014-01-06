/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package OutputMaker;

import dataArchive.Job;
import dataArchive.Machine;
import dataArchive.Order;
import dataArchive.ProcessTime;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hslf.model.Sheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import result.newpackage.I_AnswerReportContainer;
import valuationUnit.DateValuation;

/**
 *
 * @author 117
 */
public class MakeSheat {

    
    String falseReason = "特に異常なし";
    Workbook wb;
    I_AnswerReportContainer container;
    Map<String, CellStyle> styles;
    DateValuation valuationer;

    public boolean Create(File aflle, I_AnswerReportContainer acontainer, JFreeChart chartsource) throws IOException {
        container = acontainer;
        valuationer = new DateValuation(acontainer);



        
         //（2）Workbookインターフェースへの代入
         if ((!aflle.getName().isEmpty()) && aflle.getName().endsWith(".xls")) {
         wb = new HSSFWorkbook();
         } else {
          wb = new XSSFWorkbook();
         }
         
        
        
        
        

        styles = createCellStyles();

        org.apache.poi.ss.usermodel.Sheet sheet1 = wb.createSheet("実行結果");
        org.apache.poi.ss.usermodel.Sheet sheet2 = wb.createSheet("グラフ");
        
        

        //<editor-fold defaultstate="collapsed" desc="シート１">
        {//シート１
            createCell(1, 1, styles.get("HeadLine"), sheet1).setCellValue("実行結果");
            createCell(3, 1, null, sheet1).setCellValue("解法");
            createCell(3, 2, null, sheet1).setCellValue(container.getSolutionMeans());

            createCell(5, 1, null, sheet1).setCellValue("入力情報");
            int endRow1;
            endRow1 = createJobMachineArrangement(6, 1, sheet1);
            

            fillTableBlock(7, 2, container.getMachineList().size(), container.getJobList().size(), 0, sheet1);

            createCell(endRow1 + 2, 1, null, sheet1).setCellValue("結果");
            
            int endRow2;
            endRow2 = createJobMachineArrangement(endRow1 + 3, 1, sheet1);

            fillTableBlock(endRow1 + 4, 2, container.getMachineList().size(), container.getJobList().size(), 1, sheet1);

            /*
            if(!printGraph(1, 7, 600, 400, sheet1, chartsource)){
                return false;
            }
            */
                
            

        
            createCell(endRow2 + 2, 1, styles.get("HeadLine"), sheet1).setCellValue("リードタイム");

            createCell(endRow2 + 4, 1, null, sheet1).setCellValue("最大完了時間");
            createCell(endRow2 + 4, 2, null, sheet1).setCellValue(container.getReadTime());

            createCell(endRow2 + 7, 1, styles.get("Item"), sheet1);
            createCell(endRow2 + 8, 1, styles.get("Category"), sheet1).setCellValue("リードタイム");
            createCell(endRow2 + 9, 1, styles.get("Category"), sheet1).setCellValue("納期");
            createCell(endRow2 + 10, 1, styles.get("Category"), sheet1).setCellValue("余裕時間");

            createJobNameLabel(endRow2 + 7, 2, sheet1);
            createTableBlock(endRow2 + 8, 2, 3, container.getJobList().size(), sheet1);

            fillTableBlock(endRow2 + 8, 2, 1, container.getJobList().size(), 2, sheet1);

            fillTableBlock(endRow2 + 9, 2, 1, container.getJobList().size(), 3, sheet1);

            fillTableBlock(endRow2 + 10, 2, 1, container.getJobList().size(), 4, sheet1);

            createCell(endRow2 + 12, 1, styles.get("Item"), sheet1).setCellValue("最大納期遅れ時間");
            createCell(endRow2 + 13, 1, styles.get("Item"), sheet1).setCellValue("平均納期遅れ時間");
            createCell(endRow2 + 14, 1, styles.get("Item"), sheet1).setCellValue("最大納期ずれ時間");
            createCell(endRow2 + 15, 1, styles.get("Item"), sheet1).setCellValue("平均納期ずれ時間");

            createCell(endRow2 + 12, 2, styles.get("Item"), sheet1).setCellValue(valuationer.maxDeliveryLate());
            createCell(endRow2 + 13, 2, styles.get("Item"), sheet1).setCellValue(valuationer.averageDeliveryLate());
            createCell(endRow2 + 14, 2, styles.get("Item"), sheet1).setCellValue(valuationer.maxDeliveryGap());
            createCell(endRow2 + 15, 2, styles.get("Item"), sheet1).setCellValue(valuationer.averageDeliveryGap());

            
           
            /*
             * 英数字一文字分の横幅が256。
             * ×18 = 4608
             * [最大納期ずれ時間]の幅にぴったりです。
             */
            
            sheet1.setColumnWidth(1, 4608);
               

        }
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="シート２">
        {
            if(!printGraph(1, 1, container.getCountJob() * 150 , container.getCountMachine() * 100, sheet2, chartsource)){
                return false;
            }
        }
        //</editor-fold>
        
        

        
        try (FileOutputStream fileOut = new FileOutputStream(aflle)) {
            wb.write(fileOut);
        }catch(Exception e){
            falseReason = "ファイルの書き込みに失敗しました";
            return false;
                    
        }





        return true;
    }
    
    private boolean printGraph(int row,int column,int imageWidth ,int imageHeight,org.apache.poi.ss.usermodel.Sheet sheet,JFreeChart chart) throws IOException{
        

            CreationHelper createHelper = wb.getCreationHelper();

            
            byte[] bytes;
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            ChartUtilities.writeChartAsPNG(outStream, chart, imageWidth, imageHeight);
            bytes = outStream.toByteArray();
        }catch(Exception e){
            falseReason = "チャート画像の読出しに失敗しました";
            return false;
        }
            
            
            

            
            

            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

            // (2)指定した位置に画像を配置addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            Drawing patriarch = sheet.createDrawingPatriarch();
            ClientAnchor anchor = createHelper.createClientAnchor();
            anchor.setCol1(column);
            anchor.setRow1(row);
            Picture picture = patriarch.createPicture(anchor, pictureIdx);

            // (3)画像サイズを1.2倍に拡大
            picture.resize(1.0);
            
            return true;
    }

    private Row createRow(int row, org.apache.poi.ss.usermodel.Sheet sheet) {
        if (null == sheet.getRow(row)) {
            Row ROW = sheet.createRow(row);
            return ROW;
        }
        return sheet.getRow(row);

    }

    private Cell createCell(int row, int column, CellStyle style, org.apache.poi.ss.usermodel.Sheet sheet) {
        Row row1 = this.createRow(row, sheet);
        Cell cell = row1.createCell(column);
        //cell.setCellValue(str);

        if (style != null) {
            cell.setCellStyle(style);
        }

        return cell;

    }

    /*
     private Cell createCellDouble(int row,int column,Double dbr,CellStyle style,org.apache.poi.ss.usermodel.Sheet sheet){
     Row row1 = sheet.createRow(row);
     Cell cell = row1.createCell(column);
     //cell.setCellValue(dbr);
        
     if (style != null) {
     cell.setCellStyle(style);
     }
        
     return cell;
     }*/
    private Cell fillCell(int row, int column, org.apache.poi.ss.usermodel.Sheet sheet) {
        Row row1 = sheet.getRow(row);
        Cell cell = row1.getCell(column);
        //cell.setCellValue(str);

        return cell;
    }
    /*
     private Cell fillCellDouble(int row,int column,Double dbr,org.apache.poi.ss.usermodel.Sheet sheet){
     Row row1 = sheet.getRow(row);
     Cell cell = row1.getCell(column);
     //cell.setCellValue(dbr);
        
     return cell;
     }*/

    //ジョブマシン表を作る。
    private int createJobMachineArrangement(int row, int column, org.apache.poi.ss.usermodel.Sheet sheet) {

        List<Job> jobList = container.getJobList();
        List<Machine> machineList = container.getMachineList();

        //<editor-fold defaultstate="collapsed" desc="ここまで表の(0,0)に当たる基本セル">

        Row baseRow = this.createRow(row, sheet);
        Cell baseCell = baseRow.createCell(column);
        baseCell.setCellStyle(styles.get("Item"));

        //</editor-fold>


        //ジョブラベル
        createJobNameLabel(row, column + 1, sheet);


        //<editor-fold defaultstate="collapsed" desc="ここまでマシンラベル作り">

        int rowCount = row + 1;
        for (Machine machine : machineList) {
            Row headRow = this.createRow(rowCount, sheet);
            Cell headCell = headRow.createCell(column);

            headCell.setCellValue(machine.getViewName());
            headCell.setCellStyle(styles.get("Machine"));

            rowCount += 1;
        }
        //</editor-fold>


        //データブロック
        int endblock;

        endblock = createTableBlock(row + 1, column + 1, machineList.size(), jobList.size(), sheet);

        return endblock;

    }

    private int createTableBlock(int firstrow, int firstcolumn, int rowLength, int columnLength, org.apache.poi.ss.usermodel.Sheet sheet) {

        int rowCount = firstrow;
        int columnCount = firstcolumn;


        for (int i = 0; i < rowLength; i++) {
            Row row = this.createRow(rowCount, sheet);
            for (int j = 0; j < columnLength; j++) {
                Cell cell = row.createCell(columnCount);
                cell.setCellStyle(styles.get("Item"));

                columnCount += 1;

            }
            rowCount += 1;
            columnCount = firstcolumn;
        }

        return rowCount;

    }

    private void fillTableBlock(int firstrow, int firstcolumn, int rowLength, int columnLength, int cases, org.apache.poi.ss.usermodel.Sheet sheet) {

        int rowCount = firstrow;
        int columnCount = firstcolumn;

        List<Job> joblist = container.getJobList();
        List<Machine> machinelist = container.getMachineList();

        for (int i = 0; i < rowLength; i++) {
            Row row = sheet.getRow(rowCount);

            Machine machine = machinelist.get(i);


            for (int j = 0; j < columnLength; j++) {
                Cell cell = row.getCell(columnCount);


                //プログラムの作りがすごく汚い！！どうにかしたい！
                //<editor-fold defaultstate="collapsed" desc="セル内容を設定するスイッチ">
                switch (cases) {
                    case 0:
                        //ジョブ毎の processtime(order)の文字列
                        String ins;
                        ProcessTime time = container.getMachineProcessTime(joblist.get(j), machinelist.get(i));
                        Order order = container.getOrder(joblist.get(j), machinelist.get(i));

                        
                        
                        ins = time.get() + "(" + order.get() + ")";
                        
                        if (time.get() == 0) {
                            ins = "なし";
                        }
                        
                        
                        cell.setCellValue(ins);
                        break;

                    case 1:
                        //ジョブ毎の開始時間+"-"+終了時間の文字列
                        String ins2;
                        ins2 =
                                container.getStarttimeInteger(joblist.get(j), machinelist.get(i))
                                + "-"
                                + container.getEndtimeInteger(joblist.get(j), machinelist.get(i));
                        
                        if(container.getStarttimeInteger(joblist.get(j), machinelist.get(i)).equals(container.getEndtimeInteger(joblist.get(j), machinelist.get(i)))){
                            ins2 = "なし";
                        }
                        
                        cell.setCellValue(ins2);
                        break;

                    case 2:
                        //ジョブ毎のリードタイム
                        double ins3;
                        ins3 = container.getJobReadtime(joblist.get(j));
                        cell.setCellValue(ins3);

                        break;

                    case 3:
                        //納期
                        double ins4;
                        ins4 = joblist.get(j).getTheTimeOfDelivery();
                        cell.setCellValue(ins4);

                        break;

                    case 4:
                        //余裕時間 納期-リードタイム
                        double ins5;
                        ins5 = joblist.get(j).getTheTimeOfDelivery() - container.getJobReadtime(joblist.get(j));
                        cell.setCellValue(ins5);

                        break;
                    default:
                        break;
                }
                //</editor-fold>



                columnCount += 1;

            }
            rowCount += 1;
            columnCount = firstcolumn;
        }


    }

    private void createJobNameLabel(int firstrow, int firstcolumn, org.apache.poi.ss.usermodel.Sheet sheet) {
        List<Job> joblist = container.getJobList();
        Row row = this.createRow(firstrow, sheet);
        Cell cell;
        int cellCount = firstcolumn;

        //ジョブ名ラベルを生成
        for (Job job : joblist) {
            cell = row.createCell(cellCount);

            cell.setCellValue(job.getViewName());
            cell.setCellStyle(styles.get("Job"));

            cellCount += 1;
        }
    }

    private Map<String, CellStyle> createCellStyles() {
        Map<String, CellStyle> styleMap = new HashMap<>();
        CellStyle style;


        style = wb.createCellStyle();
        borderSetting(style);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setAlignment(CellStyle.ALIGN_CENTER);


        styleMap.put("Job", style);

        style = wb.createCellStyle();
        borderSetting(style);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setAlignment(CellStyle.ALIGN_CENTER);

        styleMap.put("Machine", style);

        style = wb.createCellStyle();
        borderSetting(style);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.ROSE.getIndex());


        styleMap.put("HeadLine", style);

        style = wb.createCellStyle();
        borderSetting(style);

        styleMap.put("Item", style);


        style = wb.createCellStyle();
        borderSetting(style);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());

        styleMap.put("Category", style);

        return styleMap;



    }

    private void borderSetting(CellStyle style) {

        //（3）セルスタイルへ罫線の設定
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

    }
    
    public String getFalseReason(){
        return falseReason;
    }
}
