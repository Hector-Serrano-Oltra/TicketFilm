import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class RegisterScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField FullNameField;
	private JTextField EmailField;
	private JPasswordField passwordField;
	private JPasswordField ConfirmPasswordField;
	private JTextField DirectionField;
	private JTextField DateOfBirthField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterScreen frame = new RegisterScreen();
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
	public RegisterScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel FullNameLabel = new JLabel("Nombre Completo");
		FullNameLabel.setBounds(37, 30, 101, 14);
		contentPane.add(FullNameLabel);
		
		JLabel EmailLabel = new JLabel("Correo");
		EmailLabel.setBounds(37, 55, 101, 14);
		contentPane.add(EmailLabel);
		
		JLabel PasswordLabel = new JLabel("Contraseña");
		PasswordLabel.setBounds(37, 80, 101, 14);
		contentPane.add(PasswordLabel);
		
		JLabel lblConfirmarContrasea = new JLabel("Confirmar Contraseña");
		lblConfirmarContrasea.setBounds(37, 105, 124, 14);
		contentPane.add(lblConfirmarContrasea);
		
		JLabel AddressLabel = new JLabel("Direccion");
		AddressLabel.setBounds(37, 130, 124, 14);
		contentPane.add(AddressLabel);
		
		JLabel DateOfBirthLabel = new JLabel("Fecha Nacimiento");
		DateOfBirthLabel.setBounds(37, 155, 124, 14);
		contentPane.add(DateOfBirthLabel);
		
		JRadioButton TermsOfServiceRadio = new JRadioButton("Aceptar Terminos y Condiciones");
		TermsOfServiceRadio.setBounds(37, 187, 188, 23);
		contentPane.add(TermsOfServiceRadio);
		
		JButton RegisterButton = new JButton("Registrar");
		RegisterButton.setBounds(49, 217, 89, 23);
		contentPane.add(RegisterButton);
		
		FullNameField = new JTextField();
		FullNameField.setBounds(185, 27, 86, 20);
		contentPane.add(FullNameField);
		FullNameField.setColumns(10);
		
		EmailField = new JTextField();
		EmailField.setColumns(10);
		EmailField.setBounds(185, 52, 86, 20);
		contentPane.add(EmailField);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(185, 77, 86, 20);
		contentPane.add(passwordField);
		
		ConfirmPasswordField = new JPasswordField();
		ConfirmPasswordField.setBounds(185, 102, 86, 20);
		contentPane.add(ConfirmPasswordField);
		
		DirectionField = new JTextField();
		DirectionField.setColumns(10);
		DirectionField.setBounds(185, 127, 86, 20);
		contentPane.add(DirectionField);
		
		DateOfBirthField = new JTextField();
		DateOfBirthField.setToolTipText("Pon la fecha en este formato: (DD/MM/YYYY)");
		DateOfBirthField.setColumns(10);
		DateOfBirthField.setBounds(185, 152, 86, 20);
		contentPane.add(DateOfBirthField);

	}
}
