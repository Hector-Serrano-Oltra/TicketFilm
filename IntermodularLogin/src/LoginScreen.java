import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField EmailTextField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen frame = new LoginScreen();
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
	public LoginScreen() {
		setTitle("Login Screen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel EmailLabel = new JLabel("Correo:");
		EmailLabel.setBounds(53, 42, 46, 14);
		contentPane.add(EmailLabel);
		
		JLabel PasswordLabel = new JLabel("Contraseña:");
		PasswordLabel.setBounds(53, 82, 68, 14);
		contentPane.add(PasswordLabel);
		
		JButton SendButton = new JButton("Enviar");
		SendButton.setBounds(53, 143, 89, 23);
		contentPane.add(SendButton);
		
		JLabel NoAccountLabel = new JLabel("¿No tienes cuenta?");
		NoAccountLabel.setBounds(53, 177, 109, 14);
		contentPane.add(NoAccountLabel);
		
		JButton RegisterButton = new JButton("Registrate aqui");
		RegisterButton.setBounds(53, 202, 109, 23);
		contentPane.add(RegisterButton);
		
		EmailTextField = new JTextField();
		EmailTextField.setBounds(165, 39, 86, 20);
		contentPane.add(EmailTextField);
		EmailTextField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(165, 82, 86, 20);
		contentPane.add(passwordField);

	}
}
