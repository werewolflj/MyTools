package tool.investment;

import yahoofinance.*;
import yahoofinance.histquotes.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 * Created by penet on 15/7/11.
 */
public class StockTools {
    public static void main(String[] args) {
        try{
            /*
            FileOutputStream fos = null;
            BufferedInputStream bis = null;
            HttpURLConnection httpUrl = null;
            URL url = null;
            byte[] buf = new byte[8096];
            int size = 0;

            url = new URL("http://hq.sinajs.cn/list=sh601006");
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(httpUrl.getInputStream()));

            //bis = new BufferedInputStream(httpUrl.getInputStream());

            System.out.println(bufferedReader.readLine());

            httpUrl.disconnect();
            */









            StockTools stockTools = new StockTools();
            stockTools.updateMyStocks(stockTools.getMyStocks());

            //stockTools.addStocks();

            //String filePath = "/Users/penet/IdeaProjects/MyTools/mylib/2015.txt";
            //stockTools.addStocks(filePath);

            //stockTools.readStocksFile(filePath);

            /*
            String stockid = "600008.SS";
            Calendar from = Calendar.getInstance();
            from.set(2015,4,10);
            Calendar to = Calendar.getInstance();
            to.set(2015, 4, 15);

            stockTools.getHistoryPriceDaily(stockid, from, to);
            */

            //java -cp hsqldb.jar org.hsqldb.util.DatabaseManagerSwing


        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("vivian is a pig!");
    }

    //create table stocks(id varchar(20) primary key, zfprice double, zfdate date, cq double, todayprice double, discount double, ssdate date)
    //更新数据库里面已有股票的收盘价
    public List<MyStock> updateMyStocks(List<MyStock> myStocks) throws Exception {
        String ids = "";
        for(int i = 0 ; i < myStocks.size() ; i ++) {
            MyStock myStock = myStocks.get(i);
            if(i > 0 && i % 1000 == 0) {
                ids += myStock.getId() + "|";
            }
            else {
                String id = myStock.getId();
                ids += id + ",";
            }

        }
        //System.out.println(ids);
        //System.out.println(ids.substring(0,ids.length()-1));

        String[] symbols = ids.substring(0,ids.length()-1).split(",");
        Map<String, Stock> stocks = YahooFinance.get(symbols); // single request
        List<MyStock> updateStocks = new ArrayList<MyStock>();
        for(int i = 0 ; i < myStocks.size() ; i ++) {
            MyStock myStock = myStocks.get(i);
            float price = stocks.get(myStock.getId()).getQuote().getPrice().floatValue();
            float discount = (price - myStock.getZfprice()) / myStock.getZfprice();
            myStock.setTodayprice(price);
            myStock.setDiscount(discount);
            updateStocks.add(myStock);
            System.out.println(myStock.getId() + ":" + myStock.getDiscount());
        }

        //开始更新数据库
        Connection connection = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:/Users/penet/IdeaProjects/MyTools/mylib/mydb");
            PreparedStatement pstmt = connection.prepareStatement("update stocks set todayprice=?,discount=? where id=?");

            for(int i = 0 ; i < updateStocks.size() ; i ++) {
                MyStock myStock = updateStocks.get(i);
                pstmt.setFloat(1,myStock.getTodayprice());
                pstmt.setFloat(2,myStock.getDiscount());
                pstmt.setString(3,myStock.getId());
                pstmt.executeUpdate();
            }
            pstmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null) {
                connection.close();
            }
        }

        return updateStocks;
    }


    //获取数据库里面的所有股票数据
    public List<MyStock> getMyStocks() throws Exception {
        Connection connection = null;
        List<MyStock> myStocks = new ArrayList<>();
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:/Users/penet/IdeaProjects/MyTools/mylib/mydb");
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery("select * from stocks");

            while(rst.next()) {
                MyStock myStock = new MyStock();
                myStock.setId(rst.getString("id"));
                myStock.setZfprice(rst.getFloat("zfprice"));
                myStock.setZfdate(rst.getString("zfdate"));
                myStock.setCq(rst.getFloat("cq"));
                myStock.setTodayprice(rst.getFloat("todayprice"));
                myStock.setDiscount(rst.getFloat("discount"));
                myStock.setSsdate(rst.getString("ssdate"));
                myStocks.add(myStock);
                //System.out.println(myStock.getId() + ":" + myStock.getTodayprice());
            }
            rst.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null) {
                connection.close();
            }
        }
        return myStocks;
        //statement.execute("SHUTDOWN;");

    }

    //新添加股票数据
    public void addStocks(String filePath) throws Exception {
        List<MyStock> myStocks = this.readStocksFile(filePath);
        this.addStocks(myStocks);
    }


    //新添加股票数据
    public void addStocks(List<MyStock> myStocks) throws Exception {

        Connection connection = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:/Users/penet/IdeaProjects/MyTools/mylib/mydb");
            PreparedStatement pstmt = connection.prepareStatement("insert into stocks values(?,?,?,0,0,0,?)");
            for(int i = 0 ; i < myStocks.size() ; i ++) {
                MyStock myStock = myStocks.get(i);
                pstmt.setString(1,myStock.getId());
                pstmt.setFloat(2, myStock.getZfprice());
                pstmt.setString(3, myStock.getZfdate());
                pstmt.setString(4,myStock.getSsdate());
                pstmt.executeUpdate();
            }
            pstmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null) {
                connection.close();
            }
        }
        //statement.execute("SHUTDOWN;");

    }

    public void addStocks() throws Exception {

        Connection connection = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:/Users/penet/IdeaProjects/MyTools/mylib/mydb");
            PreparedStatement pstmt = connection.prepareStatement("insert into stocks values(?,10,'2015-01-01',0,0,0,'2015-01-01')");
            int id = 300001;
            for(int i = 0 ; i < 490 ; i ++) {
                int a = id + i;
                pstmt.setString(1,a + ".SZ");
                pstmt.executeUpdate();
            }
            pstmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null) {
                connection.close();
            }
        }

    }


    //获取股票历史价格
    public List<HistoricalQuote> getHistoryPriceDaily(String stockid, Calendar from, Calendar to) throws Exception {
        Stock stock = YahooFinance.get(stockid);
        List<HistoricalQuote> stocks = stock.getHistory(from, to, Interval.DAILY);
        return stocks;
    }

    //判断是否除权了
    private boolean isCQ(List<HistoricalQuote> stocks) {
        boolean flagCQ = false;
        for (int i = 0 ; i < stocks.size() ; i ++) {
            HistoricalQuote today = stocks.get(i);
            //判断是否有除权
            if(i > 0) {
                HistoricalQuote yestoday = stocks.get(i - 1);
                //today.getAdjClose();
                float todayPrice = today.getAdjClose().floatValue();
                float yestodayPrice = yestoday.getAdjClose().floatValue();
                float a = Math.abs(todayPrice - yestodayPrice);
                if( a/yestodayPrice > 0.22) {
                    System.out.println("------------ CQ --------------" + today.getDate().getTime().toString());
                    flagCQ = true;
                }
            }
        }
        return flagCQ;
    }

    //读取文件导入数据库
    //eg. 000975.SZ 银泰资源 2021-01-25 5.0000 2013-01-24
    public List<MyStock> readStocksFile(String filePath){
        List<MyStock> myStocks = new ArrayList<>();
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                boolean firstLine = true;
                String allStockStr = "";
                while((lineTxt = bufferedReader.readLine()) != null){
                    if(firstLine) {
                        firstLine = false;
                    }
                    else {
                        String[] strs = lineTxt.split(" ");
                        String str = strs[0];

                        if(str.contains("SH")) {
                            str = str.replace("H","S");
                        }

                        MyStock myStock = new MyStock();
                        myStock.setId(str);
                        myStock.setZfprice(Float.parseFloat(strs[3]));
                        myStock.setZfdate(strs[4]);
                        myStock.setSsdate(strs[2]);
                        if(allStockStr.contains(str)) {
                            System.out.println(str + "stock exist!");
                        }
                        else {
                            allStockStr += str;
                            myStocks.add(myStock);
                            System.out.println("|" + myStock.getId() + ":" + myStock.getSsdate() + ":" + myStock.getZfprice() + ":" + myStock.getZfdate() + "|");
                        }
                    }

                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return myStocks;

    }

}
