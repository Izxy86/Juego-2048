package interfaz;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.JPanel;

import juego.Tablero;

public class Interfaz {

	private JFrame ventana;
	private JTextArea cuadroDeMsj;

	private JPanel contenedorDeBaldosas;
	private Tablero tablero;

	private JTextField cuadroPuntaje;
	private String puntajeAMostrar;

	private JPanel contenedorDePuntajesHistoricos;
	private JTextField[] cuadrosRecordsHistoricos;

	private String recordAMostrar;
	private JTextField cuadroRecord;

	// Inicia la aplicacion
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interfaz window = new Interfaz();
					window.ventana.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Crea la aplicacion
	public Interfaz() {
		initialize();
	}

	// Inicia todo lo de la ventana
	private void initialize() {

		tablero = new Tablero();
		inicio();
		ventanaPrincipal();
		leerJuegoGuardado();
		tableroDeJuego();


		botonJuegoNuevo();
		botonMostrarInstrucciones();

		puntaje();
		actualizarPuntaje();

		record();
		actualizarRecord();

		recordsHistoricos();
		actualizarRecordsHistoricos();

		// Comienza el juego
		Juego();

	}

	private void ventanaPrincipal() {
		ventana = new JFrame();
		ventana.setIconImage(Toolkit.getDefaultToolkit().getImage(Interfaz.class.getResource("/interfaz/icon.png")));
		ventana.getContentPane().setBackground(new Color(0xFAF8EF));
		ventana.setTitle("2048");
		ventana.setBounds(100, 100, 590, 530);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.getContentPane().setLayout(null);

		// Manejo del cierre de Ventana
		ventana.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		ventana.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				guardarJuego();
				close();
			}
		});

		// Titulo
		JLabel titulo = new JLabel("2048");
		titulo.setBounds(112, 11, 158, 50);
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 50));
		titulo.setForeground(new Color(143, 122, 102));
		ventana.getContentPane().add(titulo);

		// Menú
		JMenuBar menuBar = new JMenuBar();
		ventana.setJMenuBar(menuBar);

		// Menú Archivo
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		// Items de Archivo

		// Juego Nuevo
		JMenuItem mnItemJuegoNuevo = new JMenuItem("Juego Nuevo");
		mnItemJuegoNuevo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guardarJuego();
				reiniciarJuego();
			}
		});
		mnItemJuegoNuevo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnArchivo.add(mnItemJuegoNuevo);

		// Reiniciar
		JMenuItem mntmReiniciar = new JMenuItem("Reiniciar");
		mntmReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = JOptionPane.showConfirmDialog(ventana, "Confirmas que quieres reiniciar el juego?");
				if (opcion == 0) {
					guardarJuego();
					reiniciarJuego();
				}
			}
		});
		mntmReiniciar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mnArchivo.add(mntmReiniciar);

		// Salir
		JMenuItem mnItemSalir = new JMenuItem("Salir");
		mnItemSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int opcion = JOptionPane.showConfirmDialog(ventana, "Confirmas que quieres Abandonar");
				if (opcion == 0) {
					guardarJuego();
					close();
				} else {
					setMsj("Excelente! sigue Jugando" + tablero.getUsuario() + "!");
				}
			}
		});
		mnItemSalir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnArchivo.add(mnItemSalir);

		// Menú Ayuda
		JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);

		// Item de Ayuda
		JMenuItem mnAcercaDe = new JMenuItem("Acerca De...");
		mnAcercaDe.setToolTipText("Informe del Juego");
		mnAcercaDe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File ruta = new File("src/interfaz/informe.pdf");
					Desktop.getDesktop().open(ruta);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		mnAcercaDe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnAyuda.add(mnAcercaDe);

	}

	private void close() {
		if (JOptionPane.showConfirmDialog(ventana, "¿Desea Salir del Juego?", "Salir del sistema",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			guardarJuego();
			System.exit(0);
		}
	}

	public void inicio() {
		cuadroDeMsjAlUsuario();
		setUsuarioNuevo();
	}

	public void tableroDeJuego() {
		// Crea el panel de las baldosas
		contenedorDeBaldosas = new JPanel();
		contenedorDeBaldosas.setBackground(new Color(187, 173, 160));
		contenedorDeBaldosas.setBounds(70, 100, 240, 240);
		ventana.getContentPane().add(contenedorDeBaldosas);
		contenedorDeBaldosas.setLayout(null);
	}

	public void setMsj(String msj) {
		cuadroDeMsj.selectAll();
		cuadroDeMsj.replaceSelection(msj);
	}

	public void cuadroDeMsjAlUsuario() {

		cuadroDeMsj = new JTextArea(3, 3);// numero de filas y columnas
		cuadroDeMsj.setLineWrap(true);
		cuadroDeMsj.setWrapStyleWord(true);// para que haga salto de linea al final sin cortar palabras
		cuadroDeMsj.setBounds(380, 37, 90, 20);
		cuadroDeMsj.setBackground(new Color(143, 122, 102));
		cuadroDeMsj.setForeground(new Color(249, 246, 242));
		cuadroDeMsj.setFont(new Font("Tahoma", Font.BOLD, 15));
		cuadroDeMsj.setFocusable(false);
		cuadroDeMsj.setBorder(null);
		cuadroDeMsj.setMargin(new Insets(200, 500, 200, 200));
	}

	public void Juego() {
		// Agregar un KeyListener al JFrame para capturar las teclas presionadas por el
		// usuario
		ventana.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// Obtener el código de la tecla presionada
				int keyCode = e.getKeyCode();

				// Realizar una acción dependiendo de la tecla presionada
				switch (keyCode) {
				case KeyEvent.VK_W:
					if (tablero.moverArriba()) {
						verificarEstadoJuego();
					}
					break;

				case KeyEvent.VK_S:
					if (tablero.moverAbajo()) {
						verificarEstadoJuego();
					}
					break;

				case KeyEvent.VK_A:
					if (tablero.moverIzquierda()) {
						verificarEstadoJuego();
					}
					break;

				case KeyEvent.VK_D:
					if (tablero.moverDerecha()) {
						verificarEstadoJuego();
					}
					break;

				default:
					// Ignorar otras teclas
					break;
				}

				// Actualizar la interfaz después de cada movimiento
				actualizarInterfaz();
				actualizarPuntaje();
				actualizarRecord();
				actualizarRecordsHistoricos();

				// Comprobar si se ha logrado un nuevo record
				String usuarioConRecord = tablero.getUsuario();
				lograrNuevoRecord(usuarioConRecord, tablero.getPuntaje());
			}
		});

		// Hacer que el JFrame tenga el foco para que pueda recibir eventos de teclado
		ventana.requestFocus();

	}

	public void actualizarInterfaz() {
		// Obtener el estado actual del tablero
		int[][] estadoTablero = tablero.getTablero();

		// Limpiar el panel de baldosas antes de actualizarlo
		contenedorDeBaldosas.removeAll();

		// Recorrer el estado del tablero y actualizar el panel de baldosas
		for (int fila = 0; fila < Tablero.FILAS; fila++) {
			for (int col = 0; col < Tablero.COLUMNAS; col++) {
				int valorCelda = estadoTablero[fila][col];

				// Crear un nuevo JLabel para representar cada celda del tablero
				JLabel labelCelda = new JLabel(String.valueOf(valorCelda));
				labelCelda.setBounds(col * 60, fila * 60, 60, 60); // Posición y tamaño de la celda
				if (valorCelda == 0) {
					labelCelda.setForeground(new Color(0xcdc1be));
				}
				if (valorCelda < 1000) {
					labelCelda.setHorizontalAlignment(SwingConstants.CENTER);
					labelCelda.setFont(new Font("Tahoma", Font.BOLD, 24));
				} else {
					labelCelda.setHorizontalAlignment(SwingConstants.CENTER);
					labelCelda.setFont(new Font("Tahoma", Font.BOLD, 18));
				}
				// Configurar el color de fondo y el color del texto según el valor de la celda
				switch (valorCelda) {
				case 2:
					labelCelda.setBackground(new Color(238, 228, 218));
					break;
				case 4:
					labelCelda.setBackground(new Color(237, 224, 200));
					break;
				case 8:
					labelCelda.setBackground(new Color(242, 177, 121));
					break;
				case 16:
					labelCelda.setBackground(new Color(245, 149, 99));
					break;
				case 32:
					labelCelda.setBackground(new Color(246, 124, 95));
					break;
				case 64:
					labelCelda.setBackground(new Color(246, 94, 59));
					break;
				case 128:
					labelCelda.setBackground(new Color(237, 207, 114));
					break;
				case 256:
					labelCelda.setBackground(new Color(247, 204, 97));
					break;
				case 512:
					labelCelda.setBackground(new Color(236, 220, 80));
					break;
				case 1024:
					labelCelda.setBackground(new Color(227, 197, 83));
					break;
				case 2048:
					labelCelda.setBackground(new Color(250, 100, 250));
					break;

				default:
					labelCelda.setBackground(new Color(205, 193, 180));
					break;
				}

				// Configurar otros aspectos visuales de la celda
				labelCelda.setOpaque(true);
				labelCelda.setBorder(BorderFactory.createLineBorder(Color.BLACK));

				// Agregar el JLabel al panel de baldosas
				contenedorDeBaldosas.add(labelCelda);
			}
		}

		// Revalidar y repintar el panel de baldosas para que los cambios sean visibles
		contenedorDeBaldosas.revalidate();
		contenedorDeBaldosas.repaint();
	}

	public void botonJuegoNuevo() {
		JButton btnJuegoNuevo = new JButton("Juego nuevo");

		btnJuegoNuevo.setBorder(null);// Sacar los bordes que vienen por defecto
		btnJuegoNuevo.setFocusable(false);// Evitar que se vea un cuadro punteado alrededor del texto del boton
		btnJuegoNuevo.setBackground(new Color(143, 122, 102));
		btnJuegoNuevo.setForeground(new Color(249, 246, 242));
		btnJuegoNuevo.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnJuegoNuevo.setBounds(122, 379, 136, 35);
		ventana.getContentPane().add(btnJuegoNuevo);

		btnJuegoNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reiniciarJuego();
			}
		});
	}

	public void reiniciarJuego() {
		// Reiniciar el tablero
		guardarJuego();
		tablero.reiniciar();
		tablero = new Tablero();
		// Actualizar la interfaz
		actualizarInterfaz();
		// Mostrar mensaje de inicio
		inicio();
		actualizarPuntaje();
		// Solicitar el foco para capturar eventos de teclado
		ventana.requestFocus();
	}

	public void botonMostrarInstrucciones() {
		JButton btnMostrarInstrucciones = new JButton("Instrucciones");

		btnMostrarInstrucciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarInstrucciones();
			}
		});

		// Agregar el botón a la ventana
		btnMostrarInstrucciones.setBorder(null);// Sacar los bordes que vienen por defecto
		btnMostrarInstrucciones.setFocusable(false);// Evitar que se vea un cuadro punteado alrededor del texto del
													// boton
		btnMostrarInstrucciones.setBackground(new Color(143, 122, 102));
		btnMostrarInstrucciones.setForeground(new Color(249, 246, 242));
		btnMostrarInstrucciones.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnMostrarInstrucciones.setBounds(122, 420, 136, 35);
		// btnJuegoNuevo.setBounds(125, 389, 136, 35);

		ventana.getContentPane().add(btnMostrarInstrucciones);
	}

	public void puntaje() {
		cuadroPuntaje = new JTextField();
		cuadroPuntaje.setFont(new Font("Tahoma", Font.BOLD, 25));
		cuadroPuntaje.setHorizontalAlignment(SwingConstants.RIGHT);
		cuadroPuntaje.setFocusable(false);
		cuadroPuntaje.setEditable(false);
		cuadroPuntaje.setBorder(null);
		cuadroPuntaje.setBackground(new Color(187, 173, 160));
		cuadroPuntaje.setForeground(Color.WHITE);
		cuadroPuntaje.setBounds(350, 40, 90, 50);
		ventana.getContentPane().add(cuadroPuntaje);

		JLabel lblPuntaje = new JLabel("Puntaje");
		lblPuntaje.setForeground(new Color(143, 122, 102));
		lblPuntaje.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblPuntaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblPuntaje.setBounds(350, 10, 90, 23);
		ventana.getContentPane().add(lblPuntaje);
	}

	public void actualizarPuntaje() {
		puntajeAMostrar = Integer.toString(tablero.getPuntaje());
		cuadroPuntaje.setText(puntajeAMostrar);

	}

	// Metodo para verificar el estado del juego y mostrar el cartel correspondiente
	public void verificarEstadoJuego() {
		if (tablero.ganador()) {
			cartelGanaste();
		} else if (tablero.jugadorPerdio()) {
			cartelPerdiste();
		}
	}

	// Metodo para mostrar un cartel de victoria
	public void cartelGanaste() {
		JOptionPane.showMessageDialog(ventana, "¡Felicidades! Has alcanzado 2048. ¡Has ganado!", "¡Ganaste!",
				JOptionPane.INFORMATION_MESSAGE);
	}

	// Metodo para mostrar un cartel de derrota
	public void cartelPerdiste() {
		JOptionPane.showMessageDialog(ventana, "¡Lo siento! No hay más movimientos válidos. ¡Has perdido!",
				"¡Perdiste!", JOptionPane.ERROR_MESSAGE);
	}

	// Metodo para mostrar un cartel con instrucciones del juego
	public void mostrarInstrucciones() {
		// Crear el mensaje de instrucciones
		String mensaje = "CÓMO JUGAR:\n" + "Usa tus teclas W A S D para mover las fichas.\n"
				+ "Las fichas con el mismo número se fusionan en una cuando se tocan.\n"
				+ "¡Súmalos para llegar al 2048!";

		JOptionPane.showMessageDialog(ventana, mensaje, "Instrucciones", JOptionPane.INFORMATION_MESSAGE);
	}

	public void record() {

		JLabel lblRecord = new JLabel("Record");
		lblRecord.setForeground(new Color(143, 122, 102));
		lblRecord.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecord.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblRecord.setBounds(450, 10, 90, 23);
		ventana.getContentPane().add(lblRecord);

		cuadroRecord = new JTextField();
		cuadroRecord.setFont(new Font("Tahoma", Font.BOLD, 25));
		cuadroRecord.setHorizontalAlignment(SwingConstants.RIGHT);
		cuadroRecord.setFocusable(false);
		cuadroRecord.setEditable(false);
		cuadroRecord.setBorder(null);
		cuadroRecord.setBackground(new Color(187, 173, 160));
		cuadroRecord.setForeground(Color.WHITE);
		cuadroRecord.setBounds(450, 40, 90, 50);
		ventana.getContentPane().add(cuadroRecord);
	}

	public void actualizarRecord() {
		int recordActual = Integer.parseInt(tablero.records[0][1]);
		int puntajeActual = tablero.getPuntaje();

		// Solo actualizar el récord si el puntaje actual es mayor
		if (puntajeActual > recordActual) {
			String usuarioActual = tablero.getUsuario(); // Reemplaza esto con el nombre de usuario actual
			tablero.setRecord(usuarioActual, puntajeActual, 0);
			recordAMostrar = tablero.records[0][1];
			cuadroRecord.setText(recordAMostrar);
			guardarJuego();
		}
	}

	public void recordsHistoricos() {
		contenedorDePuntajesHistoricos = new JPanel();
		contenedorDePuntajesHistoricos.setBackground(new Color(187, 173, 160));
		contenedorDePuntajesHistoricos.setBounds(350, 125, 180, 210);
		ventana.getContentPane().add(contenedorDePuntajesHistoricos);
		contenedorDePuntajesHistoricos.setLayout(null);

		JLabel lblRecords = new JLabel("Records Historicos");
		lblRecords.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecords.setForeground(new Color(143, 122, 102));
		lblRecords.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblRecords.setBounds(350, 100, 190, 23);
		ventana.getContentPane().add(lblRecords);

		cuadrosRecordsHistoricos = new JTextField[5];
		int posicionX = 10;
		int posicionY = 11;

		for (int i = 0; i < cuadrosRecordsHistoricos.length; i++) {
			cuadrosRecordsHistoricos[i] = new JTextField();
			cuadrosRecordsHistoricos[i].setBorder(null);
			cuadrosRecordsHistoricos[i].setBackground(new Color(205, 193, 180));
			cuadrosRecordsHistoricos[i].setForeground(Color.WHITE);
			cuadrosRecordsHistoricos[i].setFont(new Font("Tahoma", Font.BOLD, 15));
			cuadrosRecordsHistoricos[i].setHorizontalAlignment(SwingConstants.CENTER);
			cuadrosRecordsHistoricos[i].setFocusable(false);

			cuadrosRecordsHistoricos[i].setBounds(posicionX, (posicionY), 150, 23);

			cuadrosRecordsHistoricos[i].setColumns(10);
			contenedorDePuntajesHistoricos.add(cuadrosRecordsHistoricos[i]);
		}
	}

	public void actualizarRecordsHistoricos() {
		for (int i = 0; i < cuadrosRecordsHistoricos.length; i++) {
			String usuarioConRecord = tablero.getUsuarioConRecord(i);
			String recordDeUsuario = tablero.getRecordRealizado(i);

			if (recordDeUsuario != null && !recordDeUsuario.equals("0")) {
				cuadrosRecordsHistoricos[i].setText(usuarioConRecord + " " + recordDeUsuario);
			} else {
				cuadrosRecordsHistoricos[i].setBackground(new Color(205, 193, 180));
			}
		}
	}

	public void lograrNuevoRecord(String usuario, int puntaje) {
		int indiceDelRecordMasBajo = tablero.obtenerIndiceDelRecordMasBajo();
		int recordMasBajo = Integer.parseInt(tablero.records[indiceDelRecordMasBajo][1]);

		if (puntaje > recordMasBajo) {
			tablero.setRecord(usuario, puntaje, indiceDelRecordMasBajo);
			guardarJuego(); // Guarda el juego después de lograr un nuevo récord
		}
	}

	public void setUsuarioNuevo() {
		String usuarioAMostrar = JOptionPane.showInputDialog(ventana, "Ingrese su nombre de Jugador");
		tablero.setUsuario(usuarioAMostrar);
	}

	public void guardarJuego() {
		try {
			FileOutputStream fos = new FileOutputStream("JuegoGuardado.txt");
			ObjectOutputStream out = new ObjectOutputStream(fos);

			// Guarda el tablero y los récords históricos
			out.writeObject(tablero.getUsuario());
			out.writeObject(tablero.getPuntaje());

			out.close();
		} catch (Exception ex) {
			// Maneja la excepción
		}
	}

	public void leerJuegoGuardado() {
		try {
			FileInputStream fis = new FileInputStream("juegoGuardado.txt");
			ObjectInputStream in = new ObjectInputStream(fis);

			// Lee solo el usuario y el récord
			for (int i = 0; i < tablero.records.length; i++) {
				String usuario = (String) in.readObject();
				String record = (String) in.readObject();

				tablero.setUsuarioConRecord(usuario, i);
				tablero.setRecordRealizado(record, i);
			}
			in.close();
		} catch (Exception ex) {
			// Maneja la excepción
		}
	}

}