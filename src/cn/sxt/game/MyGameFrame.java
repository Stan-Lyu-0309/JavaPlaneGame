package cn.sxt.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.xml.crypto.Data;

/**
 * �ɻ���Ϸ��������
 * @author StanL
 *
 */
public class MyGameFrame extends Frame{
	
	Image planeImg = GameUtil.getImage("images/plane.png");
	Image bg = GameUtil.getImage("images/bg.jpg");
	
	Plane plane = new Plane(planeImg,250,250);
//	Shell shell = new Shell();
	
	Shell[] shells = new Shell[50];
	
	Explode bao;
	Date startTime = new Date();
	Date endTime;
	int period;   //  ��Ϸ������ʱ�� 
	
//	Plane plane2 = new Plane(planeImg,350,250);
//	Plane plane3 = new Plane(planeImg,450,250);
	
	@Override
	public void paint(Graphics g) {   //�Զ�������
		super.paint(g);
		Color c = g.getColor();
		g.drawImage(bg, 0, 0, null);
		plane.drawSelf(g);//���ɻ�
		for (int i = 0; i < shells.length; i++) {
			shells[i].draw(g);
			
			boolean peng=shells[i].getRect().intersects(plane.getRect());
			if (peng) {
//				System.out.println("��ײ��");
				plane.live=false;
				if (bao==null) {
					bao = new Explode(plane.x, plane.y);
					
					endTime=new Date();
					period = (int)((endTime.getTime()-startTime.getTime())/1000);
				}
				
				bao.draw(g);
			}
			//��ʱ���ܣ�	������ʾ
			if (!plane.live) {
				Font f = new Font("����",Font.BOLD,50);
				g.setFont(f);
				g.setColor(Color.RED);
				g.drawString("ʱ�䣺"+period+"��", (int)plane.x, (int)plane.y);
			}	
				
		}
		
		g.setColor(c);
//		plane2.drawSelf(g);
//		plane3.drawSelf(g);
		
//		g.drawImage(plane, planeX, planeY, null);
//		planeX++;	
	}
	
	//�������Ƿ������ػ����ڣ�
	class PaintThread extends Thread{
		@Override
		public void run() {
			while(true) {
//				System.out.println("���ڻ�һ��");
				repaint();  //�ػ�
				
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//������̼������ڲ���
	class keyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			
			super.keyPressed(e);
//			System.out.println("����"+e.getKeyCode());
			plane.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
			super.keyReleased(e);
//			System.out.println("̧��"+e.getKeyCode());
			plane.minusDirection(e);
		} 
		
		
		
	}
	
	/**
	 * ��ʼ������
	 */
	public void lauchFrame() {
		setTitle("����ΰ��Ʒ");
		this.setVisible(true);
		this.setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		this.setLocation(300,300);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new PaintThread().start();   //�����ػ����ڵ��߳�
		addKeyListener(new keyMonitor()); //���������Ӽ��̵ļ���
		
		//��ʼ��50���ڵ�
		for (int i = 0; i < shells.length; i++) {
			shells[i]=new Shell();
		}
		
	}
	
	public static void main(String[] args) {
		MyGameFrame f = new MyGameFrame();
		f.lauchFrame();
	}
	
	private Image offScreenImage = null;
	
	public void update(Graphics g) {
		if (offScreenImage==null) {
			offScreenImage=this.createImage(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		}
		
		Graphics gOff=offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}

}
