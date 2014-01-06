/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FileChangeModel;

import dataArchive.JobsContainer;
import dataArchiveCreate.ArrayConvertionJob;
import dataArchiveCreate.I_convertionToJobData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shirai-Lab
 */
public class FileChanger {

    I_convertionToJobData converter;
    String stateString = "未判定";
    private boolean jobTag = false;
    private boolean machineTag = false;
    private boolean valuesTag = false;
    private boolean orderTag = false;
    private boolean limitTag = false;
    private boolean jobNameTag = false;
    private boolean machineNameTag = false;
    private boolean[] tags = {jobTag, machineTag, jobNameTag, machineNameTag, valuesTag, orderTag, limitTag};
    private String[] tagNames = {"#job", "#machine", "#jobNames", "#machineNames", "#values", "#order", "#limit"};
    private ArrayList<Integer> mixedTagIndex;
    //ここまで、Fileに必要な情報をまとめたもの
    List<Integer> jobTotal;
    List<Integer> machineTotal;
    java.util.List<java.util.List<Integer>> jobMachineProcessTimeList;
    java.util.List<java.util.List<Integer>> jobMachineOrderList;
    java.util.List<Integer> jobDeliveryList;
    List<String> jobNameList;
    List<String> machineNameList;

    //ここまで、変換に必要なデータ類
    public String getStateString() {
        return this.stateString;
    }

    public boolean fileChangeResearch(File dataText) throws FileNotFoundException, IOException {


        if (!this.fileAnalysis(dataText)) {
            return false;
        }

        if (!this.DataChange(dataText)) {
            return false;

        }


        converter = new ArrayConvertionJob(jobTotal.get(0), machineTotal.get(0), jobMachineProcessTimeList, jobMachineOrderList, jobDeliveryList, jobNameList, machineNameList);


        if (!converter.checkMemberRight()) {
            stateString = converter.getReasonCheckfalse();
            return false;
        }


        stateString = "Light!" + "job:" + jobTotal.get(0) + " " + "machine:" + machineTotal.get(0);
        return true;


    }

    public JobsContainer fileChange() {

        converter = new ArrayConvertionJob(jobTotal.get(0), machineTotal.get(0), jobMachineProcessTimeList, jobMachineOrderList, jobDeliveryList, jobNameList, machineNameList);
        JobsContainer container = converter.getJobContainer();

        return container;
    }

    //txtファイルの分析プロセス。
    private boolean fileAnalysis(File afile) throws FileNotFoundException, IOException {

        boolean returnBoolean = true;

        mixedTagIndex = new ArrayList<>();

        /*
         * チェックを行う基準
         * ①タグがすべて存在するかどうか。
         * 
         */





        BufferedReader reader = new BufferedReader(new FileReader(afile));
        reader.mark(2000);
        String line;

        //①タグがすべて存在するか判定を行う。
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("　| ", "");
            //頭文字に#がなければ判定しない。
            if (line.startsWith("#")) {
            } else {
                continue;
            }

            //タグの真偽値を設定する。つまりタグがあればtrue

            for (int i = 0; i < tagNames.length; i++) {
                if (tagNames[i].equals(line)) {

                    if (tags[i] == false) {
                        tags[i] = true;
                    } else {
                        stateString = "同じタグが複数個存在する";
                        return false;
                    }

                    mixedTagIndex.add(i);

                }
            }
        }

        //ここまででライン読み込みは終わり。

        stateString = "以下のタグがない";
        for (int i = 0; i < tags.length; i++) {
            if (tags[i] == false) {

                stateString = stateString.concat("," + tagNames[i]);
                returnBoolean = false;

            }

        }

        if (returnBoolean == false) {
            return returnBoolean;
        }

        //①ここまでで終わり タグの存在が確認できているはず。

        reader.close();




        return returnBoolean;

        /*
         //②タグの中の値を分離してすべてIntegerかどうか。
         reader.reset();

         line = "";

         //全てのラインに関して真偽を判定する。
         while ((line = reader.readLine()) != null) {

         String[] splitted;
         splitted = null;
         line = line.replaceAll("　| ", "");



         if (line.endsWith(";")) {
         splitted = dataSpritter(line);

         for (String score : splitted) {
         if (!isNumber(score)) {

         returnBoolean = false;
         stateString = "データにint型以外の値が入ってます,値:" + score;
         return returnBoolean;
         }

         }
         }
         }

         */

        //②まで





    }

    private String[] dataSpritter(String line) {
        line = line.replaceAll(";", "");
        String[] splitted = line.split(",");
        return splitted;
    }

    private boolean isNumber(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException nfex) {
            return false;
        }
    }

    private boolean checkParametaSize(String[] splitted, Integer size, String tagNames) {

        try {
            if (splitted.length != size) {
                throw new Exception("長さが不正");
            }
        } catch (Exception e) {
            stateString = tagNames + "その値は一つだけです。";
            return false;

        }

        return true;
    }

    //実際にデータの変換を行うメソッド
    private boolean DataChange(File afile) throws FileNotFoundException, IOException {






        //このメソッドを起動するという事はタグの存在が確認できている。
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(afile), "Shift-JIS"));
        reader.mark(2000);

        jobTotal = new ArrayList<>();
        //#job
        if (!dataChangeAlgolism(reader, 0, 0, jobTotal)) {
            return false;
        }

        machineTotal = new ArrayList<>();
        //#machine
        if (!dataChangeAlgolism(reader, 1, 0, machineTotal)) {
            return false;
        }


        //#jobNames
        jobNameList = new ArrayList<>();
        if (!dataChangeAlgolism(reader, 2, 3, jobNameList)) {
            return false;
        };

        //#machineNames
        machineNameList = new ArrayList<>();
        if (!dataChangeAlgolism(reader, 3, 3, machineNameList)) {
            return false;
        };




        jobMachineProcessTimeList = new ArrayList<>();

        //#value
        if (!dataChangeAlgolism(reader, 4, 1, jobMachineProcessTimeList)) {
            return false;
        }


        jobMachineOrderList = new ArrayList<>();
        List<List<String>> jobMachineOrderRoughList = new ArrayList<>();
        //もしストリングの解析を後で行う場合はCaseは4;

        //#order
        if (!dataChangeAlgolism(reader, 5, 4, jobMachineOrderRoughList)) {
            return false;
        }

        jobMachineOrderList = exchangedListStyle(jobMachineOrderRoughList);


        

        jobDeliveryList = new ArrayList<>();
        //#limit
        if (!dataChangeAlgolism(reader, 6, 2, jobDeliveryList)) {
            return false;
        }





        reader.close();

        return true;
    }

    private List<List<Integer>> exchangedListStyle(List<List<String>> alist) {
        List<List<Integer>> changedList = new ArrayList<>();


        for (int i = 0; i < alist.size(); i++) {

            List<Integer> afterList = new ArrayList<>();

            List<String> beforeList = alist.get(i);

            for (int j = 0; j < alist.get(i).size(); j++) {
                String fact = beforeList.get(j);
                Integer factAfter = Integer.parseInt(fact);

                afterList.add(factAfter);
            }

            changedList.add(afterList);
        }
        return changedList;
    }

    private boolean integerCheckData(String[] splitted) {
        //渡された一行のデータ全てにIntegerかどうかカマをかける
        boolean returnBoolean = true;
        for (int i = 0; i < splitted.length; i++) {
            if (!isNumber(splitted[i])) {

                returnBoolean = false;
                stateString = "データにint型以外の値が入ってます,値:" + splitted[i];
                return returnBoolean;
            }
        }
        return returnBoolean;
    }

    private Integer indexTagToMixedTagPosition(Integer rightIndex) {
        //4,2,1,3
        //↑
        //1,2,3,4 のrightIndexが入ってるインデックスを返す。
        //4なら、戻り値は2だね

        int position = mixedTagIndex.indexOf(rightIndex);
        return position;
    }

    private boolean dataChangeAlgolism(BufferedReader reader, Integer Indextag, Integer cases, List<?> changedData) throws IOException {

        reader.reset();



        String line;

        Integer singleLineCount = 0;



        Boolean ableScan = false;
        while ((line = reader.readLine()) != null) {


            line = line.replaceAll("　| ", "");

            //タグめっけて
            if (line.equals(tagNames[Indextag])) {
                ableScan = true;
            }

            //タグ以降に来る最後に;持ちの行をチェック。
            if (ableScan && line.endsWith(";")) {


                String[] splitted;


                //parametaCount += 1;




                splitted = dataSpritter(line);





                switch (cases) {
                    case 0:
                        //長さをチェック
                        if (!checkParametaSize(splitted, 1, tagNames[Indextag])) {
                            return false;
                        }

                        //整数じゃないデータがあるか検査
                        if (!integerCheckData(splitted)) {
                            return false;
                        }


                        singleWriteData(splitted, (List<Integer>) changedData);

                        singleLineCount += 1;

                        if (singleLineCount > 1) {
                            stateString = changedData.toString() + "の設定値が異常です。（単独のはずの値が複数）：" + singleLineCount;
                            return false;
                        }

                        break;
                    case 1:
                        //整数じゃないデータがあるか検査
                        if (!integerCheckData(splitted)) {
                            return false;
                        }

                        arrayWriteData((List<List<Integer>>) changedData, splitted);
                        break;
                    case 2:
                        //整数じゃないデータがあるか検査
                        if (!integerCheckData(splitted)) {
                            return false;
                        }

                        listWriteIntData(splitted, (List<Integer>) changedData);
                        break;

                    case 3:
                        listWriteStringData(splitted, (List<String>) changedData);

                        singleLineCount += 1;

                        if (singleLineCount > 1) {
                            stateString = changedData.toString() + "の設定値が異常です。（単独のはずの値が複数）：" + singleLineCount;
                            return false;
                        }

                        break;

                    case 4:
                        arrayFillingWriteData((List<List<String>>) changedData, splitted);



                        break;

                    default:
                        throw new AssertionError();
                }

            }//ここまでがチェック

            //ラインが次のタグ名だったときこのループをぬける
            if (indexTagToMixedTagPosition(Indextag) + 1 >= tagNames.length) {
                //もしもう次のタグがないようなら、なにもしない。このままループが終わるのを待つ。
            } else {
                int position = indexTagToMixedTagPosition(Indextag) + 1;
                if (line.equals(tagNames[mixedTagIndex.get(position)])) {
                    //次のタグをラインが読み込んだらbreak
                    break;
                }
            }


        }//while抜けるのはここ以降


        if (changedData.isEmpty()) {
            stateString = "何かの値の入力が空です。";
            return false;
        }

        return true;
    }

    private void singleWriteData(String[] splitted, List<Integer> total) {

        total.add(0, Integer.parseInt(splitted[0]));

    }

    private void arrayWriteData(List<List<Integer>> array, String[] splitted) {
        //List<Integer> listMachine = array.get(Count);
        List<Integer> listMachine = new ArrayList<>();

        for (String value : splitted) {
            listMachine.add(Integer.parseInt(value));
        }
        array.add(listMachine);

    }

    //もし、オーダーでキャンセルされるものがｄだったとき用いる。
    private void arrayFillingWriteData(List<List<String>> array, String[] splitted) {
        List<String> listMachine = new ArrayList<>();
        Integer errorNumber = Integer.MIN_VALUE;


        //オーダーリストの作成
        List<Integer> ordersList = new ArrayList<>();
        for (int i = 1; i <= machineTotal.get(0); i++) {
            ordersList.add(i);
        }


        
        
        //オーダーリストからスプリット行に要素があるものを抜く。
        for (String fact : splitted) {
            if (isNumber(fact)) {

                Integer factNum = Integer.parseInt(fact);

                if (ordersList.contains(factNum)) {

                    ordersList.remove(factNum);
                }
            }
        }


        //リストの中の文字"d"を残ったオーダーリストの要素で埋める
        //もしdじゃなくてIntegerなら、ふつうにリストに押し込む。
        for (String fact : splitted) {
            if (fact.equals("d")) {

                //もし内容がdだった場合の入力↓
                if (!ordersList.isEmpty()) {



                    Integer orderMember = ordersList.get(0);
                    ordersList.remove(0);

                    listMachine.add(orderMember.toString());


                } else {

                    listMachine.add(errorNumber.toString());

                }
                //もし内容がdだった場合の入力↑ここまで

            } else if (isNumber(fact)) {
                //もしdoubleになったりすると対応できない。
                listMachine.add(fact);


            } else {
                listMachine.add(errorNumber.toString());
            }
        }

        array.add(listMachine);



    }

    private void listWriteIntData(String[] splitted, List<Integer> listJob) {
        for (String value : splitted) {
            listJob.add(Integer.parseInt(value));
        }

    }

    private void listWriteStringData(String[] splitted, List<String> listJob) {
        for (String value : splitted) {
            listJob.add(value);
        }

    }

    private List<List<Integer>> mappingXYList(List<List<Integer>> list) {
        int sizeCount = 0;
        for (List<Integer> list1 : list) {
            if (sizeCount < list1.size()) {
                sizeCount = list1.size();
            }

        }

        List<List<Integer>> mappedList = new ArrayList<>();
        for (int i = 0; i < sizeCount; i++) {
            List<Integer> colist = new ArrayList<>();

        }



        return null;
    }
}
