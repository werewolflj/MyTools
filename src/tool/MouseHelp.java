package tool;

/**
 * Created by penet on 16/3/13.
 */


import java.awt.Dimension;
        import java.awt.MouseInfo;
        import java.awt.Point;
        import java.awt.Robot;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.MouseEvent;
        import java.awt.event.MouseMotionListener;
        import javax.swing.JButton;
        import javax.swing.JFrame;
        import javax.swing.WindowConstants;

public class MouseHelp extends javax.swing.JPanel implements MouseMotionListener {
    private JButton textButton;
    Robot robot;
    /**
     * Auto-generated main method to display this
     * JPanel inside a new JFrame.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new MouseHelp());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public MouseHelp() {
        super();
        initGUI();
    }
    private void initGUI() {
        try {
            robot=new Robot();
            addMouseMotionListener(this);
            setPreferredSize(new Dimension(1024, 768));
            this.setLayout(null);
            {
                textButton = new JButton();
                this.add(textButton);
                textButton.setText("\u8fd0   \u884c");
                textButton.setBounds(136, 72, 127, 22);
                textButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        robot.mouseMove(30,745);
                        try {
                            Thread.sleep(1000);
                            robot.mousePress(MouseEvent.BUTTON1_MASK);
                            robot.mouseRelease(MouseEvent.BUTTON1_MASK);
                            Thread.sleep(1000);
                            robot.mouseMove(150,481);
                            robot.mousePress(MouseEvent.BUTTON1_MASK);
                            robot.mouseRelease(MouseEvent.BUTTON1_MASK);
                        } catch (InterruptedException e1) {
// TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mouseDragged(MouseEvent e) {
// TODO Auto-generated method stub
    }
    public void mouseMoved(MouseEvent e) {
// TODO Auto-generated method stub
//从这里获取鼠标的全局坐标
        Point mousepoint = MouseInfo.getPointerInfo().getLocation();
        System.out.println(mousepoint.x+"\t"+mousepoint.y);
    }
}