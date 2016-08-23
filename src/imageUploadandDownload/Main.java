package imageUploadandDownload;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.omg.CORBA.portable.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.io.File;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;



public class Main extends JFrame {

	private JPanel contentPane;
	JLabel lblImagesHere;
	JLabel label;
	public static String imgurl="";
	
  

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Chose Images");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Onclick Images Button this will need from here tooo ************************
				
				 JFileChooser file = new JFileChooser();
		          file.setCurrentDirectory(new File(System.getProperty("user.home")));
		          //filter the files
		          FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
		          file.addChoosableFileFilter(filter);
		          int result = file.showSaveDialog(null);
		           //if the user click on save in Jfilechooser
		          if(result == JFileChooser.APPROVE_OPTION){
		              File selectedFile = file.getSelectedFile();
		              String path = selectedFile.getAbsolutePath();
		              lblImagesHere.setIcon(ResizeImage(path));
		              System.out.println(path);
		              imgurl = path;
		          }
		           //if the user click on save in Jfilechooser


		          else if(result == JFileChooser.CANCEL_OPTION){
		              System.out.println("No File Select");
		          }
		          
		        //here ***********************************************************
			}
		});
		
		
		btnNewButton.setFont(new Font("Stencil", Font.PLAIN, 15));
		btnNewButton.setBounds(39, 183, 181, 49);
		contentPane.add(btnNewButton);
		
		lblImagesHere = new JLabel("image view");
		lblImagesHere.setHorizontalAlignment(SwingConstants.CENTER);
		lblImagesHere.setBounds(39, 23, 181, 126);
		contentPane.add(lblImagesHere);
		
		JButton btnUpload = new JButton("UPLOAD");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				
				 HttpClient httpclient = new DefaultHttpClient();
				    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

				    HttpPost httppost = new HttpPost("http://localhost/imgUpDwn/upload.php");
				    File file = new File(imgurl);

				    MultipartEntity mpEntity = new MultipartEntity();
				    ContentBody cbFile = new FileBody(file, "image/jpeg");
				    mpEntity.addPart("userfile", cbFile);


				    httppost.setEntity(mpEntity);
				    System.out.println("executing request " + httppost.getRequestLine());
				    HttpResponse response = null;
					try {
						response = httpclient.execute(httppost);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						label.setIcon(new ImageIcon(Main.class.getResource("/img/xx.png")));
					}
				    HttpEntity resEntity = response.getEntity();

				    System.out.println(response.getStatusLine());
				    if (resEntity != null) {
				      try {
						System.out.println(EntityUtils.toString(resEntity));
					} catch (ParseException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						label.setIcon(new ImageIcon(Main.class.getResource("/img/xx.png")));
					}
				    }
				    if (resEntity != null) {
				      try {
						resEntity.consumeContent();
						label.setIcon(new ImageIcon(Main.class.getResource("/img/okkk.png")));
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						label.setIcon(new ImageIcon(Main.class.getResource("/img/xx.png")));
						
					}
				    }

				    httpclient.getConnectionManager().shutdown();
				
				 
			}
		});
		btnUpload.setBounds(301, 195, 89, 23);
		contentPane.add(btnUpload);
		
		label = new JLabel("");
		label.setIcon(new ImageIcon(Main.class.getResource("/img/send.png")));
		label.setBounds(290, 47, 110, 137);
		contentPane.add(label);
	}
	
	public ImageIcon ResizeImage(String ImagePath) ///this method resize images for image view
    {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(lblImagesHere.getWidth(), lblImagesHere.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
	
	
}
