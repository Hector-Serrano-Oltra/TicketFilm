package Ventanas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import Clases.DatosApp;
import Clases.Usuario;

public class CuentaScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Color FONDO   = new Color(40, 45, 54);
	private static final Color PANEL   = new Color(52, 58, 68);
	private static final Color CLARO   = new Color(62, 67, 78);
	private static final Color TEXTO   = new Color(228, 232, 240);
	private static final Color T_LABEL = new Color(168, 174, 185);
	private static final Color BOTON   = new Color(72, 78, 90);
	private static final Color BORDE   = new Color(78, 84, 96);

	private JPanel cardPanel;
	private CardLayout cards;
	private JPanel panelLogin, panelLogueado;
	private JTextField loginDniField;
	private JPasswordField loginPassField;

	public static void main(String[] args) {
		DatosApp.cargarDatos();
		new CuentaScreen().setVisible(true);
	}

	public CuentaScreen() {
		setTitle("Acceso Administrador - TiketFilm");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel cp = new JPanel();
		cp.setBackground(FONDO);
		cp.setPreferredSize(new Dimension(400, 300));
		cp.setLayout(null);
		setContentPane(cp);

		cards = new CardLayout();
		cardPanel = new JPanel(cards);
		cardPanel.setOpaque(false);
		cardPanel.setBounds(12, 12, 376, 276);
		cp.add(cardPanel);

		crearPanelLogin();
		crearPanelLogueado();

		Usuario u = DatosApp.getUsuarioLogueado();
		if (u != null && u.getRol().equals("Admin")) {
			actualizarPanelLogueado();
			cards.show(cardPanel, "logueado");
		} else {
			cards.show(cardPanel, "login");
		}

		pack();
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((pantalla.width - getWidth()) / 2, (pantalla.height - getHeight()) / 2);
	}

	private void crearPanelLogin() {
		panelLogin = new JPanel();
		panelLogin.setBackground(PANEL);
		panelLogin.setBorder(new LineBorder(BORDE, 1));
		panelLogin.setLayout(null);

		JLabel tit = new JLabel("TIKETFILM", JLabel.CENTER);
		tit.setFont(new Font("Dialog", Font.BOLD, 22));
		tit.setForeground(TEXTO);
		tit.setBounds(0, 16, 376, 32);
		panelLogin.add(tit);

		JLabel sub = new JLabel("Acceso Administradores", JLabel.CENTER);
		sub.setFont(new Font("Dialog", Font.PLAIN, 11));
		sub.setForeground(T_LABEL);
		sub.setBounds(0, 48, 376, 18);
		panelLogin.add(sub);

		JPanel sep = new JPanel(); sep.setBackground(BORDE);
		sep.setBounds(35, 72, 306, 1); panelLogin.add(sep);

		loginDniField = campo(panelLogin, "DNI", 35, 84);
		loginPassField = campoPass(panelLogin, "Contrase\u00f1a", 35, 134);

		JButton btnLogin = crearBoton("Iniciar Sesi\u00f3n");
		btnLogin.setBounds(35, 195, 306, 36);
		panelLogin.add(btnLogin);

		btnLogin.addActionListener(e -> {
			String dni = loginDniField.getText().trim();
			String pass = new String(loginPassField.getPassword());
			if (dni.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "Rellene todos los campos."); return; }
			Usuario u = DatosApp.login(dni, pass);
			if (u == null) { JOptionPane.showMessageDialog(this, "DNI o contrase\u00f1a incorrectos."); return; }
			if (!u.getRol().equals("Admin")) { JOptionPane.showMessageDialog(this, "Solo administradores pueden acceder."); return; }
			DatosApp.setUsuarioLogueado(u);
			actualizarPanelLogueado();
			cards.show(cardPanel, "logueado");
		});

		cardPanel.add(panelLogin, "login");
	}

	private void crearPanelLogueado() {
		panelLogueado = new JPanel();
		panelLogueado.setBackground(PANEL);
		panelLogueado.setBorder(new LineBorder(BORDE, 1));
		panelLogueado.setLayout(null);
		cardPanel.add(panelLogueado, "logueado");
		actualizarPanelLogueado();
	}

	private void actualizarPanelLogueado() {
		panelLogueado.removeAll();
		Usuario u = DatosApp.getUsuarioLogueado();
		if (u == null) return;

		JLabel icono = new JLabel("\uD83D\uDC64", JLabel.CENTER);
		icono.setFont(new Font("Dialog", Font.PLAIN, 40));
		icono.setForeground(TEXTO);
		icono.setBounds(0, 16, 376, 48);
		panelLogueado.add(icono);

		JLabel nombre = new JLabel(u.getNombre() + " (" + u.getRol() + ")", JLabel.CENTER);
		nombre.setFont(new Font("Dialog", Font.BOLD, 14));
		nombre.setForeground(TEXTO);
		nombre.setBounds(0, 66, 376, 24);
		panelLogueado.add(nombre);

		JButton btnAdmin = crearBoton("Abrir Panel de Administraci\u00f3n");
		btnAdmin.setBounds(50, 120, 276, 38);
		panelLogueado.add(btnAdmin);

		JButton btnCerrar = crearBoton("Cerrar Sesi\u00f3n");
		btnCerrar.setBounds(50, 170, 276, 36);
		panelLogueado.add(btnCerrar);

		btnAdmin.addActionListener(e -> new AdminPanel().setVisible(true));
		btnCerrar.addActionListener(e -> {
			DatosApp.setUsuarioLogueado(null);
			loginDniField.setText("");
			loginPassField.setText("");
			cards.show(cardPanel, "login");
		});

		panelLogueado.revalidate();
		panelLogueado.repaint();
	}

	private JTextField campo(JPanel p, String label, int x, int y) {
		JLabel l = new JLabel(label);
		l.setFont(new Font("Dialog", Font.BOLD, 12));
		l.setForeground(T_LABEL);
		l.setBounds(x, y, 306, 18);
		p.add(l);
		JTextField f = new JTextField();
		f.setBounds(x, y + 20, 306, 28);
		f.setBackground(CLARO);
		f.setForeground(TEXTO);
		f.setBorder(new LineBorder(BORDE, 1));
		f.setCaretColor(TEXTO);
		p.add(f);
		return f;
	}

	private JPasswordField campoPass(JPanel p, String label, int x, int y) {
		JLabel l = new JLabel(label);
		l.setFont(new Font("Dialog", Font.BOLD, 12));
		l.setForeground(T_LABEL);
		l.setBounds(x, y, 306, 18);
		p.add(l);
		JPasswordField f = new JPasswordField();
		f.setBounds(x, y + 20, 306, 28);
		f.setBackground(CLARO);
		f.setForeground(TEXTO);
		f.setBorder(new LineBorder(BORDE, 1));
		f.setCaretColor(TEXTO);
		p.add(f);
		return f;
	}

	private JButton crearBoton(String txt) {
		JButton b = new JButton(txt);
		b.setFont(new Font("Dialog", Font.BOLD, 12));
		b.setBackground(BOTON);
		b.setForeground(TEXTO);
		b.setFocusPainted(false);
		return b;
	}
}
