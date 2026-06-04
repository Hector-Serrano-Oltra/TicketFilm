package Ventanas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import Clases.Admin;
import Clases.Anuncio;
import Clases.Categoria;
import Clases.Cine;
import Clases.DatosApp;
import Clases.Pelicula;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class AdminPanel extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Color FONDO   = new Color(40, 45, 54);
	private static final Color PANEL   = new Color(52, 58, 68);
	private static final Color TEXTO   = new Color(228, 232, 240);
	private static final Color T_LABEL = new Color(168, 174, 185);
	private static final Color BOTON   = new Color(72, 78, 90);
	private static final Color BORDE   = new Color(78, 84, 96);

	private static final int W = 760, H = 720;

	private JTextField txtNombrePeli, txtDescPeli, txtPrecioPeli, txtFechaPeli, txtEstadoPeli, txtUrlPeli;
	private JComboBox comboCinePeli, comboCatPeli, comboEditarPeli;
	private Pelicula peliculaEditando;

	private JTextField txtNombreCine, txtUbicacionCine, txtAforoCine;
	private JComboBox comboOpenCine, comboEditarCine;

	private JComboBox comboEditarCat;
	private JTextField txtNombreCat, txtDescCat;
	private JTextField txtFechaExp, txtFechaPub;
	private JComboBox comboPeliAnuncio, comboCineAnuncio, comboActivoAnuncio, comboGestionarAnuncio;

	public AdminPanel() {
		setTitle("Panel de Administraci\u00f3n - TiketFilm");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel cp = new JPanel();
		cp.setBackground(FONDO);
		cp.setPreferredSize(new Dimension(W, H));
		cp.setLayout(null);
		setContentPane(cp);

		int cw = 366, gx = 8;
		int xL = 10, xR = xL + cw + gx;
		int hU = 410, hL = 270;

		crearPelicula(cp, xL, 0, cw, hU);
		crearCine(cp, xR, 0, cw, hU);
		crearCategoria(cp, xL, 0, cw, hL);
		crearAnuncio(cp, xR, 0, cw, hL);
		actualizarCombos();

		pack();
		reposicionarPaneles(cp, hU, hL);
		Dimension pantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((pantalla.width - getWidth()) / 2, (pantalla.height - getHeight()) / 2);
	}

	private void reposicionarPaneles(JPanel cp, int hU, int hL) {
		int ancho = cp.getWidth();
		int alto = cp.getHeight();
		int gap = 10;
		int cw = 366;
		int yT = (alto - (hU + gap + hL)) / 2;
		int yB = yT + hU + gap;
		int xL = (ancho - (cw + gap + cw)) / 2;
		int xR = xL + cw + gap;

		java.awt.Component[] comps = cp.getComponents();
		comps[0].setLocation(xL, yT);
		comps[1].setLocation(xR, yT);
		comps[2].setLocation(xL, yB);
		comps[3].setLocation(xR, yB);
	}

	private JPanel panel(int x, int y, int w, int h) {
		JPanel p = new JPanel();
		p.setBackground(PANEL);
		p.setBorder(new LineBorder(BORDE, 1));
		p.setLayout(null);
		p.setBounds(x, y, w, h);
		return p;
	}

	private void tit(JPanel p, String txt, int pw) {
		JLabel t = new JLabel(txt, JLabel.CENTER);
		t.setForeground(TEXTO); t.setFont(new Font("Dialog", Font.BOLD, 13));
		t.setBounds(0, 8, pw, 24); p.add(t);
	}

	private void sep(JPanel p, int pw) {
		JPanel s = new JPanel(); s.setBackground(BORDE);
		s.setBounds(15, 34, pw - 30, 1); p.add(s);
	}

	private void lbl(JPanel p, String txt, int x, int y, int w) {
		JLabel l = new JLabel(txt);
		l.setForeground(T_LABEL); l.setFont(new Font("Dialog", Font.PLAIN, 12));
		l.setBounds(x, y, w, 22); p.add(l);
	}

	private void limpiarPeli() {
		txtNombrePeli.setText(""); txtDescPeli.setText(""); txtPrecioPeli.setText("");
		txtUrlPeli.setText(""); peliculaEditando = null;
	}

	private boolean fechaValida(String s) {
		if (s == null || s.length() != 10) return false;
		if (s.charAt(4) != '-' || s.charAt(7) != '-') return false;
		try {
			int a = Integer.parseInt(s.substring(0, 4));
			int m = Integer.parseInt(s.substring(5, 7));
			int d = Integer.parseInt(s.substring(8, 10));
			if (a < 2000 || a > 2100 || m < 1 || m > 12 || d < 1 || d > 31) return false;
			if ((m == 4 || m == 6 || m == 9 || m == 11) && d > 30) return false;
			if (m == 2) {
				boolean bisiesto = (a % 4 == 0 && a % 100 != 0) || (a % 400 == 0);
				if (d > (bisiesto ? 29 : 28)) return false;
			}
			return true;
		} catch (NumberFormatException e) { return false; }
	}

	// ============ PEL\u00cdCULAS ============

	private void crearPelicula(JPanel parent, int px, int py, int pw, int ph) {
		JPanel p = panel(px, py, pw, ph);
		parent.add(p);
		tit(p, "PEL\u00cdCULAS", pw);
		sep(p, pw);
		int lx = 15, fx = 105, fw = pw - fx - 15, y = 45, g = 27;

		lbl(p, "Nombre:", lx, y, 85);
		txtNombrePeli = new JTextField(); txtNombrePeli.setBounds(fx, y, fw, 22); p.add(txtNombrePeli);
		y += g; lbl(p, "Descripci\u00f3n:", lx, y, 85);
		txtDescPeli = new JTextField(); txtDescPeli.setBounds(fx, y, fw, 22); p.add(txtDescPeli);
		y += g; lbl(p, "Precio:", lx, y, 85);
		txtPrecioPeli = new JTextField(); txtPrecioPeli.setBounds(fx, y, fw, 22); p.add(txtPrecioPeli);
		y += g; lbl(p, "Fecha:", lx, y, 85);
		txtFechaPeli = new JTextField("2026-06-04"); txtFechaPeli.setBounds(fx, y, fw, 22); p.add(txtFechaPeli);
		y += g; lbl(p, "Estado:", lx, y, 85);
		txtEstadoPeli = new JTextField("Activo"); txtEstadoPeli.setBounds(fx, y, fw, 22); p.add(txtEstadoPeli);
		y += g; lbl(p, "Cine:", lx, y, 85);
		comboCinePeli = new JComboBox(); comboCinePeli.setBounds(fx, y, fw, 22); p.add(comboCinePeli);
		y += g; lbl(p, "Categor\u00eda:", lx, y, 85);
		comboCatPeli = new JComboBox(); comboCatPeli.setBounds(fx, y, fw, 22); p.add(comboCatPeli);
		y += g; lbl(p, "URL Imagen:", lx, y, 85);
		txtUrlPeli = new JTextField(); txtUrlPeli.setBounds(fx, y, fw, 22); p.add(txtUrlPeli);

		int btnY = y + 32;
		JButton btnCrear = crearBoton("Guardar");
		btnCrear.setBounds(lx, btnY, 105, 28); p.add(btnCrear);
		JButton btnNuevo = crearBoton("Limpiar");
		btnNuevo.setBounds(lx + 120, btnY, 105, 28); p.add(btnNuevo);

		JPanel sep2 = new JPanel(); sep2.setBackground(BORDE);
		sep2.setBounds(15, btnY + 36, pw - 30, 1); p.add(sep2);

		int mgY = btnY + 46;
		lbl(p, "Gestionar:", lx, mgY, 70);
		comboEditarPeli = new JComboBox();
		comboEditarPeli.setBounds(85, mgY, 255, 22); p.add(comboEditarPeli);
		JButton btnEditar = crearBoton("Editar");
		btnEditar.setBounds(lx, mgY + 30, 100, 24); btnEditar.setFont(new Font("Dialog", Font.BOLD, 11)); p.add(btnEditar);
		JButton btnEliminar = crearBoton("Eliminar");
		btnEliminar.setBounds(lx + 115, mgY + 30, 100, 24); btnEliminar.setFont(new Font("Dialog", Font.BOLD, 11)); p.add(btnEliminar);

		btnCrear.addActionListener(e -> {
			String nom = txtNombrePeli.getText().trim(), desc = txtDescPeli.getText().trim(), pre = txtPrecioPeli.getText().trim();
			if (nom.isEmpty() || desc.isEmpty() || pre.isEmpty()) { JOptionPane.showMessageDialog(null, "Rellene nombre, descripci\u00f3n y precio."); return; }
			String fec = txtFechaPeli.getText().trim();
			if (!fechaValida(fec)) { JOptionPane.showMessageDialog(null, "Fecha inv\u00e1lida. Use el formato YYYY-MM-DD (ej: 2026-06-04)."); return; }
			Cine ci = (Cine) comboCinePeli.getSelectedItem();
			Categoria ca = (Categoria) comboCatPeli.getSelectedItem();
			if (ci == null || ca == null) { JOptionPane.showMessageDialog(null, "Necesita cine y categor\u00eda."); return; }
			int pr;
			try { pr = Integer.parseInt(pre); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(null, "Precio inv\u00e1lido."); return; }
			boolean ok;
			if (peliculaEditando != null) {
				peliculaEditando.setNombre(nom);
				peliculaEditando.setDescripcion(desc);
				peliculaEditando.setPrecio(pr);
				peliculaEditando.setFecha_creacion(fec);
				peliculaEditando.setEstado(txtEstadoPeli.getText().trim());
				peliculaEditando.setPk_cine(ci.getPk());
				peliculaEditando.setNombre_categoria(ca.getNombre());
				peliculaEditando.setUrlImagen(txtUrlPeli.getText().trim());
				ok = DatosApp.actualizarPeliculaBD(peliculaEditando);
				if (ok) JOptionPane.showMessageDialog(null, "Pel\u00edcula actualizada.");
			} else {
				Pelicula nueva = new Pelicula(nom, desc, pr, fec, txtEstadoPeli.getText().trim(), ci, ca, txtUrlPeli.getText().trim());
				ok = DatosApp.insertarPeliculaBD(nueva);
				if (ok) JOptionPane.showMessageDialog(null, "Pel\u00edcula '" + nom + "' creada.");
			}
			if (ok) { actualizarCombos(); limpiarPeli(); }
		});
		btnNuevo.addActionListener(e -> limpiarPeli());
		btnEditar.addActionListener(e -> {
			Pelicula sel = (Pelicula) comboEditarPeli.getSelectedItem();
			if (sel == null) { JOptionPane.showMessageDialog(null, "Seleccione una pel\u00edcula."); return; }
			peliculaEditando = sel;
			txtNombrePeli.setText(sel.getNombre());
			txtDescPeli.setText(sel.getDescripcion());
			txtPrecioPeli.setText(String.valueOf(sel.getPrecio()));
			txtFechaPeli.setText(sel.getFecha_creacion());
			txtEstadoPeli.setText(sel.getEstado());
			txtUrlPeli.setText(sel.getUrlImagen() != null ? sel.getUrlImagen() : "");
			for (int i = 0; i < comboCinePeli.getItemCount(); i++) {
				Cine c = (Cine) comboCinePeli.getItemAt(i);
				if (c.getPk().equals(sel.getPk_cine())) { comboCinePeli.setSelectedIndex(i); break; }
			}
			for (int i = 0; i < comboCatPeli.getItemCount(); i++) {
				Categoria ca = (Categoria) comboCatPeli.getItemAt(i);
				if (ca.getNombre().equals(sel.getNombre_categoria())) { comboCatPeli.setSelectedIndex(i); break; }
			}
		});
		btnEliminar.addActionListener(e -> {
			Pelicula sel = (Pelicula) comboEditarPeli.getSelectedItem();
			if (sel == null) { JOptionPane.showMessageDialog(null, "Seleccione una pel\u00edcula."); return; }
			int resp = JOptionPane.showConfirmDialog(null, "\u00bfEliminar '" + sel.getNombre() + "' y sus anuncios?", "Confirmar", JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.YES_OPTION) {
				DatosApp.getAnuncios().removeIf(a -> a.getPk_pelicula().equals(sel.getPk()));
				if (DatosApp.eliminarPeliculaBD(sel))
				JOptionPane.showMessageDialog(null, "Pel\u00edcula eliminada.");
				actualizarCombos();
				if (peliculaEditando == sel) limpiarPeli();
			}
		});
	}

	// ============ CINES ============

	private void crearCine(JPanel parent, int px, int py, int pw, int ph) {
		JPanel p = panel(px, py, pw, ph);
		parent.add(p);
		tit(p, "CINES", pw);
		sep(p, pw);
		int lx = 15, fx = 105, fw = pw - fx - 15, y = 45, g = 27;

		lbl(p, "Nombre:", lx, y, 85);
		txtNombreCine = new JTextField(); txtNombreCine.setBounds(fx, y, fw, 22); p.add(txtNombreCine);
		y += g; lbl(p, "Ubicaci\u00f3n:", lx, y, 85);
		txtUbicacionCine = new JTextField(); txtUbicacionCine.setBounds(fx, y, fw, 22); p.add(txtUbicacionCine);
		y += g; lbl(p, "Aforo:", lx, y, 85);
		txtAforoCine = new JTextField("200"); txtAforoCine.setBounds(fx, y, fw, 22); p.add(txtAforoCine);
		y += g; lbl(p, "Abierto:", lx, y, 85);
		comboOpenCine = new JComboBox(new String[]{"S\u00ed", "No"});
		comboOpenCine.setBounds(fx, y, fw, 22); p.add(comboOpenCine);

		JButton btnCrear = crearBoton("Crear Cine");
		btnCrear.setBounds(lx, y + 30, 120, 26); p.add(btnCrear);

		JPanel sep2 = new JPanel(); sep2.setBackground(BORDE);
		sep2.setBounds(15, y + 64, pw - 30, 1); p.add(sep2);

		int mgY = y + 74;
		lbl(p, "Gestionar:", lx, mgY, 70);
		comboEditarCine = new JComboBox();
		comboEditarCine.setBounds(85, mgY, 200, 22); p.add(comboEditarCine);
		JButton btnToggle = crearBoton("Cerrar");
		btnToggle.setBounds(lx, mgY + 28, 80, 24); btnToggle.setFont(new Font("Dialog", Font.BOLD, 10)); p.add(btnToggle);
		JButton btnElimCine = crearBoton("Eliminar");
		btnElimCine.setBounds(lx + 95, mgY + 28, 80, 24); btnElimCine.setFont(new Font("Dialog", Font.BOLD, 10)); p.add(btnElimCine);

		comboEditarCine.addActionListener(e -> {
			Cine c = (Cine) comboEditarCine.getSelectedItem();
			if (c != null) btnToggle.setText(c.isIs_open() ? "Cerrar" : "Abrir");
		});

		btnCrear.addActionListener(e -> {
			String nom = txtNombreCine.getText().trim(), ubi = txtUbicacionCine.getText().trim(), af = txtAforoCine.getText().trim();
			if (nom.isEmpty() || ubi.isEmpty() || af.isEmpty()) { JOptionPane.showMessageDialog(null, "Rellene todos los campos."); return; }
			int aforo;
			try { aforo = Integer.parseInt(af); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(null, "Aforo inv\u00e1lido."); return; }
			Cine nuevo = new Cine(nom, ubi, comboOpenCine.getSelectedItem().equals("S\u00ed"), aforo);
			if (DatosApp.insertarCineBD(nuevo))
				JOptionPane.showMessageDialog(null, "Cine '" + nom + "' creado.");
			actualizarCombos();
			txtNombreCine.setText(""); txtUbicacionCine.setText(""); txtAforoCine.setText("");
		});

		btnToggle.addActionListener(e -> {
			Cine c = (Cine) comboEditarCine.getSelectedItem();
			if (c == null) { JOptionPane.showMessageDialog(null, "Seleccione un cine."); return; }
			c.setIs_open(!c.isIs_open());
			DatosApp.toggleCineBD(c);
			btnToggle.setText(c.isIs_open() ? "Cerrar" : "Abrir");
			JOptionPane.showMessageDialog(null, "Cine " + (c.isIs_open() ? "abierto" : "cerrado") + ".");
			actualizarCombos();
		});

		btnElimCine.addActionListener(e -> {
			Cine c = (Cine) comboEditarCine.getSelectedItem();
			if (c == null) { JOptionPane.showMessageDialog(null, "Seleccione un cine."); return; }
			int resp = JOptionPane.showConfirmDialog(null, "\u00bfEliminar '" + c.getNombre() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.YES_OPTION) {
				if (DatosApp.eliminarCineBD(c))
				JOptionPane.showMessageDialog(null, "Cine eliminado.");
				actualizarCombos();
			}
		});
	}

	// ============ CATEGOR\u00cdAS ============

	private void crearCategoria(JPanel parent, int px, int py, int pw, int ph) {
		JPanel p = panel(px, py, pw, ph);
		parent.add(p);
		tit(p, "CATEGOR\u00cdAS", pw);
		sep(p, pw);
		int lx = 15, fx = 105, fw = pw - fx - 15, y = 45, g = 27;

		lbl(p, "Nombre:", lx, y, 85);
		txtNombreCat = new JTextField(); txtNombreCat.setBounds(fx, y, fw, 22); p.add(txtNombreCat);
		y += g; lbl(p, "Descripci\u00f3n:", lx, y, 85);
		txtDescCat = new JTextField(); txtDescCat.setBounds(fx, y, fw, 22); p.add(txtDescCat);

		JButton btnCrear = crearBoton("Crear Categor\u00eda");
		btnCrear.setBounds(lx, y + 30, 170, 26); p.add(btnCrear);

		JPanel sep2 = new JPanel(); sep2.setBackground(BORDE);
		sep2.setBounds(15, y + 64, pw - 30, 1); p.add(sep2);

		int mgY = y + 74;
		lbl(p, "Gestionar:", lx, mgY, 70);
		comboEditarCat = new JComboBox();
		comboEditarCat.setBounds(85, mgY, 255, 22); p.add(comboEditarCat);
		JButton btnEditar = crearBoton("Editar");
		btnEditar.setBounds(lx, mgY + 30, 100, 24); btnEditar.setFont(new Font("Dialog", Font.BOLD, 11)); p.add(btnEditar);
		JButton btnEliminar = crearBoton("Eliminar");
		btnEliminar.setBounds(lx + 115, mgY + 30, 100, 24); btnEliminar.setFont(new Font("Dialog", Font.BOLD, 11)); p.add(btnEliminar);

		btnCrear.addActionListener(e -> {
			String nom = txtNombreCat.getText().trim(), desc = txtDescCat.getText().trim();
			if (nom.isEmpty() || desc.isEmpty()) { JOptionPane.showMessageDialog(null, "Rellene todos los campos."); return; }
			Admin admin = (Admin) DatosApp.getUsuarioLogueado();
			Categoria nueva = new Categoria(nom, desc, admin);
			if (DatosApp.insertarCategoriaBD(nueva))
				JOptionPane.showMessageDialog(null, "Categor\u00eda '" + nom + "' creada.");
			actualizarCombos();
			txtNombreCat.setText(""); txtDescCat.setText("");
		});

		btnEditar.addActionListener(e -> {
			Categoria sel = (Categoria) comboEditarCat.getSelectedItem();
			if (sel == null) { JOptionPane.showMessageDialog(null, "Seleccione una categor\u00eda."); return; }
			sel.setNombre(txtNombreCat.getText().trim());
			sel.setDescripcion(txtDescCat.getText().trim());
			DatosApp.actualizarCategoriaBD(sel);
			JOptionPane.showMessageDialog(null, "Categor\u00eda actualizada.");
			actualizarCombos();
			txtNombreCat.setText(""); txtDescCat.setText("");
		});

		btnEliminar.addActionListener(e -> {
			Categoria sel = (Categoria) comboEditarCat.getSelectedItem();
			if (sel == null) { JOptionPane.showMessageDialog(null, "Seleccione una categor\u00eda."); return; }
			int resp = JOptionPane.showConfirmDialog(null, "\u00bfEliminar '" + sel.getNombre() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.YES_OPTION) {
				if (DatosApp.eliminarCategoriaBD(sel))
				JOptionPane.showMessageDialog(null, "Categor\u00eda eliminada.");
				actualizarCombos();
			}
		});

		comboEditarCat.addActionListener(e -> {
			Categoria sel = (Categoria) comboEditarCat.getSelectedItem();
			if (sel != null) {
				txtNombreCat.setText(sel.getNombre());
				txtDescCat.setText(sel.getDescripcion());
			}
		});
	}

	// ============ ANUNCIOS ============

	private void crearAnuncio(JPanel parent, int px, int py, int pw, int ph) {
		JPanel p = panel(px, py, pw, ph);
		parent.add(p);
		tit(p, "ANUNCIOS", pw);
		sep(p, pw);
		int lx = 15, fx = 95, fw = pw - fx - 15, y = 45, g = 27;

		lbl(p, "Pel\u00edcula:", lx, y, 75);
		comboPeliAnuncio = new JComboBox(); comboPeliAnuncio.setBounds(fx, y, fw, 22); p.add(comboPeliAnuncio);
		y += g; lbl(p, "Cine:", lx, y, 75);
		comboCineAnuncio = new JComboBox(); comboCineAnuncio.setBounds(fx, y, fw, 22); p.add(comboCineAnuncio);
		y += g; lbl(p, "Expira:", lx, y, 50);
		txtFechaExp = new JTextField("2026-12-31"); txtFechaExp.setBounds(fx, y, 100, 22); p.add(txtFechaExp);
		comboActivoAnuncio = new JComboBox(new String[]{"Activo", "Inactivo"});
		comboActivoAnuncio.setBounds(fx + 110, y, 80, 22); p.add(comboActivoAnuncio);
		y += g; lbl(p, "Publicado:", lx, y, 70);
		txtFechaPub = new JTextField("2026-06-04"); txtFechaPub.setBounds(fx, y, 100, 22); p.add(txtFechaPub);

		JButton btnCrear = crearBoton("Crear Anuncio");
		btnCrear.setBounds(lx, y + 30, 120, 26); p.add(btnCrear);

		JPanel sepA = new JPanel(); sepA.setBackground(BORDE);
		sepA.setBounds(15, y + 64, pw - 30, 1); p.add(sepA);

		int mgY = y + 74;
		lbl(p, "Gestionar:", lx, mgY, 70);
		comboGestionarAnuncio = new JComboBox();
		comboGestionarAnuncio.setBounds(85, mgY, 200, 22); p.add(comboGestionarAnuncio);
		JButton btnToggle = crearBoton("Activar");
		btnToggle.setBounds(lx, mgY + 28, 80, 24); btnToggle.setFont(new Font("Dialog", Font.BOLD, 10)); p.add(btnToggle);
		JButton btnElimAn = crearBoton("Eliminar");
		btnElimAn.setBounds(lx + 95, mgY + 28, 80, 24); btnElimAn.setFont(new Font("Dialog", Font.BOLD, 10)); p.add(btnElimAn);

		comboGestionarAnuncio.addActionListener(e -> {
			Anuncio a = (Anuncio) comboGestionarAnuncio.getSelectedItem();
			if (a != null) btnToggle.setText(a.isActivo() ? "Desactivar" : "Activar");
		});

		btnCrear.addActionListener(e -> {
			Pelicula pel = (Pelicula) comboPeliAnuncio.getSelectedItem();
			Cine cin = (Cine) comboCineAnuncio.getSelectedItem();
			if (pel == null || cin == null) { JOptionPane.showMessageDialog(null, "Necesita pel\u00edcula y cine."); return; }
			String exp = txtFechaExp.getText().trim(), pub = txtFechaPub.getText().trim();
			if (!fechaValida(exp) || !fechaValida(pub)) { JOptionPane.showMessageDialog(null, "Fecha inv\u00e1lida. Use YYYY-MM-DD."); return; }
			if (exp.compareTo(pub) <= 0) { JOptionPane.showMessageDialog(null, "La fecha de expiraci\u00f3n debe ser posterior a la de publicaci\u00f3n."); return; }
			Anuncio nuevo = new Anuncio(exp, pub, comboActivoAnuncio.getSelectedItem().equals("Activo"), pel, cin);
			if (DatosApp.insertarAnuncioBD(nuevo))
				JOptionPane.showMessageDialog(null, "Anuncio creado para '" + pel.getNombre() + "'.");
			actualizarCombos();
		});

		btnToggle.addActionListener(e -> {
			Anuncio a = (Anuncio) comboGestionarAnuncio.getSelectedItem();
			if (a == null) { JOptionPane.showMessageDialog(null, "Seleccione un anuncio."); return; }
			a.setActivo(!a.isActivo());
			DatosApp.toggleAnuncioBD(a);
			btnToggle.setText(a.isActivo() ? "Desactivar" : "Activar");
			JOptionPane.showMessageDialog(null, "Anuncio " + (a.isActivo() ? "activado" : "desactivado") + ".");
			actualizarCombos();
		});

		btnElimAn.addActionListener(e -> {
			Anuncio a = (Anuncio) comboGestionarAnuncio.getSelectedItem();
			if (a == null) { JOptionPane.showMessageDialog(null, "Seleccione un anuncio."); return; }
			int resp = JOptionPane.showConfirmDialog(null, "\u00bfEliminar anuncio #" + a.getPk() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.YES_OPTION) {
				if (DatosApp.eliminarAnuncioBD(a))
				JOptionPane.showMessageDialog(null, "Anuncio eliminado.");
				actualizarCombos();
			}
		});
	}

	// ============ COMBOS ============

	public void actualizarCombos() {
		Object selPeli = comboEditarPeli != null ? comboEditarPeli.getSelectedItem() : null;
		Object selAnun = comboGestionarAnuncio != null ? comboGestionarAnuncio.getSelectedItem() : null;
		Object selCine = comboEditarCine != null ? comboEditarCine.getSelectedItem() : null;
		Object selCat  = comboEditarCat != null ? comboEditarCat.getSelectedItem() : null;

		comboCinePeli.removeAllItems(); comboCatPeli.removeAllItems();
		comboPeliAnuncio.removeAllItems(); comboCineAnuncio.removeAllItems();
		comboEditarPeli.removeAllItems(); comboGestionarAnuncio.removeAllItems();
		comboEditarCine.removeAllItems(); comboEditarCat.removeAllItems();

		for (Cine c : DatosApp.getCines()) { comboCinePeli.addItem(c); comboCineAnuncio.addItem(c); comboEditarCine.addItem(c); }
		for (Categoria c : DatosApp.getCategorias()) { comboCatPeli.addItem(c); comboEditarCat.addItem(c); }
		for (Pelicula p : DatosApp.getPeliculas()) { comboPeliAnuncio.addItem(p); comboEditarPeli.addItem(p); }
		for (Anuncio a : DatosApp.getAnuncios()) { comboGestionarAnuncio.addItem(a); }

		if (selPeli != null) comboEditarPeli.setSelectedItem(selPeli);
		if (selAnun != null) comboGestionarAnuncio.setSelectedItem(selAnun);
		if (selCine != null) comboEditarCine.setSelectedItem(selCine);
		if (selCat  != null) comboEditarCat.setSelectedItem(selCat);
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
