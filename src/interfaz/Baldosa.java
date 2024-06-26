package interfaz;

import java.awt.Color;

public class Baldosa {
	private static final int BALDOSA_VACIA = 0;

	private int valor;

	public Baldosa() {
		this(BALDOSA_VACIA);
	}

	public Baldosa(int valor) {
		this.valor = valor;
	}

	public boolean estaVacia() {
		return valor == BALDOSA_VACIA;
	}

	public Color obtenerColorBaldosa() {
		switch (valor) {
		case 2:
			return new Color(ColorBaldosas.COLOR_2.obtenerColorHex());
		case 4:
			return new Color(ColorBaldosas.COLOR_4.obtenerColorHex());
		case 8:
			return new Color(ColorBaldosas.COLOR_8.obtenerColorHex());
		case 16:
			return new Color(ColorBaldosas.COLOR_16.obtenerColorHex());
		case 32:
			return new Color(ColorBaldosas.COLOR_32.obtenerColorHex());
		case 64:
			return new Color(ColorBaldosas.COLOR_64.obtenerColorHex());
		case 128:
			return new Color(ColorBaldosas.COLOR_128.obtenerColorHex());
		case 256:
			return new Color(ColorBaldosas.COLOR_256.obtenerColorHex());
		case 512:
			return new Color(ColorBaldosas.COLOR_512.obtenerColorHex());
		case 1024:
			return new Color(ColorBaldosas.COLOR_1024.obtenerColorHex());
		case 2048:
			return new Color(ColorBaldosas.COLOR_2048.obtenerColorHex());
		}

		return new Color(ColorBaldosas.COLOR_0.obtenerColorHex());
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
}