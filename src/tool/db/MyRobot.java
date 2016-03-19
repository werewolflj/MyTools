package tool.db;

import java.awt.*;
import java.util.Random;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;


/**
 * Created by penet on 15/11/19.
 */
public class MyRobot {
    public MyRobot() {
    }

    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            robot.delay(5000);

            MyRobot myRobot = new MyRobot();

            //做庄绳玩法
            myRobot.doChainBet(robot, 100);


            /*
            for(int i = 0 ; i < 2 ; i ++) {
                Point mousepoint = MouseInfo.getPointerInfo().getLocation();
                System.out.println(mousepoint.x+"\t"+mousepoint.y);
                myRobot.showColor(robot,mousepoint.x,mousepoint.y);
                robot.delay(5000);
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //对冲
    private void doMyBet(Robot robot,int betCount) {

        Random random = new Random();
        for(int i = 0 ; i < betCount ; i ++) {
            this.checkBegin(robot);
            System.out.println("第 " + i + " 轮：");
            //robot.delay(1000);

            int a = random.nextInt(10) % 2;

            this.chooseMoney(0, 0, 1, robot);  //选择第二个砝码
            this.chooseSide(0, 0, a, robot);
            this.doSure(0, 0, robot);

            robot.delay(1000);
            this.chooseMoney(1000, 0, 1, robot);
            this.chooseSide(1000, 0, 1 - a, robot);
            this.doSure(1000, 0, robot);

            robot.delay(10000);

            this.checkEnd(robot);
            robot.delay(5000);
        }
    }


    //绳式下注法，用的庄下注
    private void doChainBet(Robot robot,int betCount) {
        int count = 0;
        int winCounter = 0;
        for(int i = 0 ; i < betCount ; i ++) {
            this.checkBegin(robot);
            System.out.println("第 " + i + " 轮：");
            //robot.delay(1000);
            this.chooseMoney(0,0,1,robot);  //选择第二个砝码
            //int a = random.nextInt(10) % 2;

            for(int j = 0 ; j < Math.pow(2,count) ; j ++) {
                this.chooseSide(0, 0, 0, robot);  //投注庄
                //robot.delay(500);
            }
            this.doSure(0,0,robot);

            this.checkEnd(robot);
            robot.delay(5000);

            boolean in = true;
            boolean Xwin = false;
            boolean Zxin = false;
            while(!this.checkColor(robot, 958, 428, 255, 255, 255)) {
                if(in) {
                    if(this.checkXWin(robot)) {
                        in = false;
                        Xwin = true;
                    }
                    else if(this.checkZWin(robot)) {
                        in = false;
                        Zxin = true;
                    }
                    robot.delay(1000);
                }

            }
            if (Xwin) {
                count += 1;
            }
            else if(Zxin) {
                count = 0;
                winCounter += 1;
            }

            //robot.delay(20000);
        }

        System.out.println("win: " + winCounter);
    }


    //254	533
    //750	538
    //770	532
    //选择庄还是闲，0是庄，1是闲
    private void chooseSide(int initX, int initY, int x, Robot robot) {
        if(x == 0) {
            robot.mouseMove(initX + 770, initY + 532); //庄
        }
        else {
            robot.mouseMove(initX + 254, initY + 532); //闲
        }
        robot.mousePress(BUTTON1_DOWN_MASK); //点击砝码
        robot.delay(200);
        robot.mouseRelease(BUTTON1_DOWN_MASK);
        robot.delay(500);
    }

    //344	632    第一个砝码
    //395	633    第二个砝码
    //395	633
    //选择第几个砝码投注
    private void chooseMoney(int initX, int initY, int y, Robot robot) {
        int basex = 345 + initX;
        int basey = 632 + initY;
        robot.mouseMove(basex + 50*y, basey); //第一个视频下注砝码的坐标
        robot.mousePress(BUTTON1_DOWN_MASK); //点击砝码
        robot.delay(200);
        robot.mouseRelease(BUTTON1_DOWN_MASK);
        robot.delay(300);
        robot.mousePress(BUTTON1_DOWN_MASK); //点击砝码
        robot.delay(200);
        robot.mouseRelease(BUTTON1_DOWN_MASK);

        robot.delay(1000);
    }

    //点击确认
    private void doSure(int initX, int initY, Robot robot) {

        robot.mouseMove(initX + 764, initY + 610); //确认
        robot.mousePress(BUTTON1_DOWN_MASK); //点击砝码
        robot.delay(200);
        robot.mouseRelease(BUTTON1_DOWN_MASK);
        robot.delay(1000);
    }

    //点击取消
    private void doCancel(Robot robot) {
        robot.mouseMove(818,617); //取消
        robot.mousePress(BUTTON1_DOWN_MASK); //点击砝码
        robot.delay(200);
        robot.mouseRelease(BUTTON1_DOWN_MASK);
        robot.delay(2000);
    }


    //检查是否开始投注了
    private void checkBegin(Robot robot) {
        //检查是否是白色
        while(!this.checkColor(robot, 958, 428, 255,255,255)) {
            robot.delay(1000);
        }
    }

    //检查是否开始投注了
    private void checkEnd(Robot robot) {
        //检查是否是白色
        while(this.checkColor(robot, 958, 428, 255,255,255)) {
            robot.delay(1000);
        }
    }


    //检查颜色
    private boolean checkColor(Robot robot, int x, int y, int rcolor,int gcolor, int bcolor) {
        boolean isColor = false;
        Color color = robot.getPixelColor(x, y);
        if(color.getRed()==rcolor && color.getGreen()==gcolor && color.getBlue()==bcolor) {
            isColor = true;
            //System.out.println("开始投注");
        }
        else {
            //System.out.println("还没开始");
        }
        return isColor;
        //System.out.println("Red = " + color.getRed());
        //System.out.println("Green = " + color.getGreen());
        //System.out.println("Blue = " + color.getBlue());
    }

    private void showColor(Robot robot,int x,int y) {
        Color color = robot.getPixelColor(x, y);
        System.out.println("Red = " + color.getRed());
        System.out.println("Green = " + color.getGreen());
        System.out.println("Blue = " + color.getBlue());
    }


    //检查是否是闲赢了
    private boolean checkXWin(Robot robot) {
        boolean flag = false;
        for(int i = 0 ; i < 20 ; i ++) {
            if(this.checkColor(robot, 287,529,86,79,118)) {
                flag = true;
                System.out.println("闲赢");
            }
            robot.delay(100);
        }

        return flag;

    }

    //检查是否是庄赢了
    private boolean checkZWin(Robot robot) {
        boolean flag = false;
        for(int i = 0 ; i < 20 ; i ++) {
            if(this.checkColor(robot, 736,526,86,79,118)) {
                flag = true;
                System.out.println("庄赢");
            }
            robot.delay(100);
        }
        return flag;
    }

    //287	529  闲坐标
    //赢得时候颜色
    //Red = 86
    //Green = 79
    //Blue = 118
    //平时颜色
    //41,32,79
}
