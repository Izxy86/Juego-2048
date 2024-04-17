package juego;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")

public class Tablero implements Serializable {

	public static final int FILAS = 4;
	public static final int COLUMNAS = 4;

	private int[][] tablero;
	private int puntaje = 0;
	public String[][] records;
	private String usuario;

	public Tablero() {
		tablero = new int[FILAS][COLUMNAS];

		for (int fila = 0; fila < FILAS; fila++) {
			for (int col = 0; col < COLUMNAS; col++) {
				tablero[fila][col] = 0;
			}
		}

		ponerDosOCuatro();
		ponerDosOCuatro();

		records = new String[5][2];
		for (int i = 0; i < records.length; i++) {
			records[i][0] = "";
			records[i][1] = "0";
		}
	}

	// Se termina la partida si el jugador llego a 2048 o se quedo sin espacios
	public boolean finPartida() {
		return ganador() || vacias() == 0;
	}

	// Revisa si el jugador llego a 2048
	public boolean ganador() {
		for (int fila = 0; fila < FILAS; fila++)
			for (int col = 0; col < COLUMNAS; col++)
				if (tablero[fila][col] == 2048)
					return true;

		return false;
	}

	// Metodo para verificar si el jugador perdió
	public boolean jugadorPerdio() {
		// El jugador pierde si el tablero está lleno y no hay movimientos válidos
		return !movimientoValido() && vacias() == 0 && !haySumaPosible();
	}

	// Metodo auxiliar para verificar si hay movimientos válidos
	private boolean movimientoValido() {
		// El jugador puede mover si hay al menos una combinación posible en el tablero
		for (int fila = 0; fila < FILAS; fila++) {
			for (int col = 0; col < COLUMNAS; col++) {
				int valorActual = tablero[fila][col];
				// Verificar movimientos arriba, abajo, izquierda y derecha
				if ((fila > 0 && valorActual == tablero[fila - 1][col])
						|| (fila < FILAS - 1 && valorActual == tablero[fila + 1][col])
						|| (col > 0 && valorActual == tablero[fila][col - 1])
						|| (col < COLUMNAS - 1 && valorActual == tablero[fila][col + 1])) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean haySumaPosible() {
		// Verificar si hay alguna combinación posible al mover las casillas
		for (int fila = 0; fila < FILAS; fila++) {
			for (int col = 0; col < COLUMNAS; col++) {
				int valorActual = tablero[fila][col];
				// Verificar movimientos arriba, abajo, izquierda y derecha
				if ((fila > 0 && valorActual == tablero[fila - 1][col])
						|| (fila < FILAS - 1 && valorActual == tablero[fila + 1][col])
						|| (col > 0 && valorActual == tablero[fila][col - 1])
						|| (col < COLUMNAS - 1 && valorActual == tablero[fila][col + 1])) {
					return true;
				}
			}
		}
		return false;
	}

	// Cuenta la cantidad de 0s en el tablero
	public int vacias() {
		int cont = 0;
		for (int fila = 0; fila < FILAS; fila++) {
			for (int col = 0; col < COLUMNAS; col++) {
				if (tablero[fila][col] == 0)
					cont++;
			}
		}
		return cont;
	}

	// Agrega un 2 o 4 aleatoriamente en el tablero
	private void ponerDosOCuatro() {
		int fila;
		int col;

		Random random = new Random();

		do {
			fila = random.nextInt(FILAS);
		} while (vaciasEnFila(fila) == 0);
		do {
			col = random.nextInt(COLUMNAS);
		} while (tablero[fila][col] != 0);

		tablero[fila][col] = elegirNumero();
	}

	private int elegirNumero() {
		Random random = new Random();
		double probabilidad = random.nextDouble();// Genera un número aleatorio entre 0 y 1. si es entre 0 y 0,8 elije
		// 2 y si es entre 0,81 y 1 elije 4

		if (probabilidad < 0.80) { // 80% de probabilidad de 2 y 20% de probabilidad de 4
			return 2;
		} else {
			return 4;
		}
	}

	// Cuenta los 0s en una fila
	private int vaciasEnFila(int fila) {
		int cont = 0;
		for (int col = 0; col < COLUMNAS; col++) {
			if (tablero[fila][col] == 0)
				cont++;
		}
		return cont;
	}

	// Cuenta los 0s en una columna
	@SuppressWarnings("unused")
	private int vaciasEnColumna(int col) {
		int cont = 0;
		for (int fila = 0; fila < FILAS; fila++) {
			if (tablero[fila][col] == 0)
				cont++;
		}
		return cont;
	}

	// -------------------------------------------------------------------------------------------------------------------//
	// MOVIMIENTO HACIA ARRIBA//
	// -------------------------------------------------------------------------------------------------------------------//

	public boolean moverArriba() {
		boolean movio = false;

		// Mover numeros hacia arriba para cada columna
		for (int col = 0; col < tablero.length; col++) {
			int insertarPos = 0;
			for (int fila = 0; fila < tablero.length; fila++) {
				if (tablero[fila][col] != 0) {
					if (fila != insertarPos) {
						// Mueve los numeros para insertar la posicion
						tablero[insertarPos][col] = tablero[fila][col];
						tablero[fila][col] = 0;
						movio = true;
					}
					insertarPos++;
				}
			}

			for (int fila = 0; fila < tablero.length - 1; fila++) {
				if (tablero[fila][col] != 0 && tablero[fila][col] == tablero[fila + 1][col]) {
					tablero[fila][col] *= 2;
					tablero[fila + 1][col] = 0;
					puntaje += tablero[fila][col]; // Update score
					movio = true;
				}
			}

			insertarPos = 0;
			for (int fila = 0; fila < tablero.length; fila++) {
				if (tablero[fila][col] != 0) {
					if (fila != insertarPos) {
						// Mueve los numeros para insertar la posicion despues de unirlos
						tablero[insertarPos][col] = tablero[fila][col];
						tablero[fila][col] = 0;
					}
					insertarPos++;
				}
			}
		}
		if (!finPartida())
			ponerDosOCuatro();
		return movio;
	}

	// -------------------------------------------------------------------------------------------------------------------//
	// MOVIMIENTO HACIA ABAJO //
	// -------------------------------------------------------------------------------------------------------------------//

	public boolean moverAbajo() {
		boolean movio = false;

		// Mover numeros hacia abajo para cada columna
		for (int col = 0; col < tablero.length; col++) {
			// Comprimir numeros hasta el fondo
			int insertarPos = tablero.length - 1;
			for (int fila = tablero.length - 1; fila >= 0; fila--) {
				if (tablero[fila][col] != 0) {
					if (fila != insertarPos) {
						tablero[insertarPos][col] = tablero[fila][col];
						tablero[fila][col] = 0;
						movio = true;
					}
					insertarPos--;
				}
			}

			// Combina los numeros identicos adjacentes para hacer una suma
			for (int fila = tablero.length - 1; fila > 0; fila--) {
				if (tablero[fila][col] != 0 && tablero[fila][col] == tablero[fila - 1][col]) {
					tablero[fila][col] *= 2;
					tablero[fila - 1][col] = 0;
					puntaje += tablero[fila][col]; // Actualiza el puntaje
					movio = true;
				}
			}

			// Comprime los numeros hacia abajo luego de la union
			insertarPos = tablero.length - 1;
			for (int fila = tablero.length - 1; fila >= 0; fila--) {
				if (tablero[fila][col] != 0) {
					if (fila != insertarPos) {
						// Mueve los numeros para insertar la posicion despues de unirlos
						tablero[insertarPos][col] = tablero[fila][col];
						tablero[fila][col] = 0;
					}
					insertarPos--;
				}
			}
		}
		if (!finPartida())
			ponerDosOCuatro();
		return movio;
	}

	// -------------------------------------------------------------------------------------------------------------------//
	// MOVIMIENTO HACIA DERECHA //
	// -------------------------------------------------------------------------------------------------------------------//

	public boolean moverDerecha() {
		boolean movio = false;

		// Mover numeros hacia derecha para cada columna
		for (int fila = 0; fila < tablero.length; fila++) {
			// Apila los numeros a la derecha
			int insertarPos = tablero.length - 1;
			for (int col = tablero.length - 1; col >= 0; col--) {
				if (tablero[fila][col] != 0) {
					if (col != insertarPos) {
						// Mueve los numeros para insertar la posicion
						tablero[fila][insertarPos] = tablero[fila][col];
						tablero[fila][col] = 0;
						movio = true;
					}
					insertarPos--;
				}
			}

			for (int col = tablero.length - 1; col > 0; col--) {
				if (tablero[fila][col] != 0 && tablero[fila][col] == tablero[fila][col - 1]) {
					// Une los numeros iguales
					tablero[fila][col] *= 2;
					tablero[fila][col - 1] = 0;
					puntaje += tablero[fila][col]; // Actualiza el puntaje
					movio = true;
				}
			}

			// Comprime los numeros hacia la derecha luego de la union
			insertarPos = tablero.length - 1;
			for (int col = tablero.length - 1; col >= 0; col--) {
				if (tablero[fila][col] != 0) {
					if (col != insertarPos) {
						tablero[fila][insertarPos] = tablero[fila][col];
						tablero[fila][col] = 0;
					}
					insertarPos--;
				}
			}
		}
		if (!finPartida())
			ponerDosOCuatro();
		return movio;
	}

	// -------------------------------------------------------------------------------------------------------------------//
	// MOVIMIENTO HACIA IZQUIERDA //
	// -------------------------------------------------------------------------------------------------------------------//

	public boolean moverIzquierda() {
		boolean movio = false;

		// Mueve numeros hacia izquierda para cada columna
		for (int fila = 0; fila < tablero.length; fila++) {
			int insertarPos = 0;
			for (int col = 0; col < tablero.length; col++) {
				if (tablero[fila][col] != 0) {
					if (col != insertarPos) {
						// Mueve los numeros para insertar la posicion
						tablero[fila][insertarPos] = tablero[fila][col];
						tablero[fila][col] = 0;
						movio = true;
					}
					insertarPos++;
				}
			}

			// Combina los numeros identicos adjacentes para hacer una suma
			for (int col = 0; col < tablero.length - 1; col++) {
				if (tablero[fila][col] != 0 && tablero[fila][col] == tablero[fila][col + 1]) {
					tablero[fila][col] *= 2;
					tablero[fila][col + 1] = 0;
					puntaje += tablero[fila][col]; // Actualiza el puntaje

					movio = true;
				}
			}

			// Comprime los numeros hacia la izquierda luego de la union
			insertarPos = 0;
			for (int col = 0; col < tablero.length; col++) {
				if (tablero[fila][col] != 0) {
					if (col != insertarPos) {
						// Mueve los numeros para insertar la posicion despues de unirlos
						tablero[fila][insertarPos] = tablero[fila][col];
						tablero[fila][col] = 0;
					}
					insertarPos++;
				}
			}
		}
		if (!finPartida())
			ponerDosOCuatro();
		return movio;
	}

	void controlDePuntajes() {

		for (int i = records.length - 1; i >= 0; i--) {

			int actual = i;
			int siguiente = actual + 1;
			if (puntaje > getRecord(actual)) {
				if (actual == 4) {
					setUsuarioConRecord(getUsuario(), actual);
					setRecordRealizado(Integer.toString(getPuntaje()), actual);
				} else {
					setUsuarioConRecord(getUsuarioConRecord(actual), siguiente);
					setUsuarioConRecord(getUsuario(), actual);

					setRecordRealizado(Integer.toString(getRecord(actual)), siguiente);
					setRecordRealizado(Integer.toString(getPuntaje()), actual);
				}
			}
		}

		setPuntaje(0);
	}

	public int obtenerIndiceDelRecordMasBajo() {
		int indiceDelRecordMasBajo = 0;
		int recordMasBajo = Integer.parseInt(records[0][1]);

		for (int i = 1; i < records.length; i++) {
			int recordActual = Integer.parseInt(records[i][1]);
			if (recordActual < recordMasBajo) {
				recordMasBajo = recordActual;
				indiceDelRecordMasBajo = i;
			}
		}
		return indiceDelRecordMasBajo;
	}

	public void reiniciar() {
		controlDePuntajes();
	}

	// --------------------------------------------------------------------------------//
	// GETTERS Y SETTERS //
	// --------------------------------------------------------------------------------//

	public int[][] getTablero() {
		return tablero;
	}

	public int getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {

		if (usuario == null || usuario.equals("")) {
			this.usuario = "Anonimo";
		} else if (usuario.length() > 8) {
			this.usuario = "";
			for (int i = 0; i < 8; i++) {
				this.usuario += usuario.charAt(i);
			}
		} else {
			this.usuario = usuario;
		}
	}

	public int getRecord(int posicion) {
		return Integer.parseInt(records[posicion][1]);
	}

	public void setRecord(String usuario, int puntaje, int posicion) {
		if (posicion >= 0 && posicion < records.length) {
			// Actualiza el record y el usuario en la posición especificada
			records[posicion][0] = usuario;
			records[posicion][1] = Integer.toString(puntaje);
		} else {
			// Lanza una excepción si la posición no es válida
			throw new IndexOutOfBoundsException("Posición de récord no válida: " + posicion);
		}
	}

	public String getUsuarioConRecord(int posicion) {
		return records[posicion][0];
	}

	public void setUsuarioConRecord(String usuario, int posicion) {
		this.records[posicion][0] = usuario;
	}

	public String getRecordRealizado(int posicion) {
		return records[posicion][1];
	}

	public void setRecordRealizado(String record, int posicion) {
		this.records[posicion][1] = record;
	}
}