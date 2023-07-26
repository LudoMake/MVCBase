package vistas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;
import excepciones.BBDDException;
import excepciones.CantidadDebeSerPositivaException;
import modelo.Editorial;
import net.miginfocom.swing.MigLayout;

public class NuevaEditorialDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private Controlador controlador;
	private JSpinner spinnerAnio;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NuevaEditorialDialog dialog = new NuevaEditorialDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NuevaEditorialDialog() {
		setBounds(100, 100, 464, 327);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow][grow][][grow][]", "[][][][][][][][]"));
		{
			JLabel lblNewLabel = new JLabel("Introduce los datos de la editorial:");
			lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
			contentPanel.add(lblNewLabel, "cell 1 1 5 1,growx");
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Nombre:");
			lblNewLabel_1.setFont(new Font("Verdana", Font.PLAIN, 12));
			contentPanel.add(lblNewLabel_1, "cell 1 3,alignx trailing");
		}
		{
			txtNombre = new JTextField();
			contentPanel.add(txtNombre, "cell 2 3 5 1,growx");
			txtNombre.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Año:");
			lblNewLabel_1.setFont(new Font("Verdana", Font.PLAIN, 12));
			contentPanel.add(lblNewLabel_1, "cell 1 5,alignx right");
		}
		{
			spinnerAnio = new JSpinner();
			spinnerAnio.setModel(
					new SpinnerNumberModel(Integer.valueOf(2023), Integer.valueOf(1900), null, Integer.valueOf(1)));
			contentPanel.add(spinnerAnio, "cell 2 5,growx");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Insertar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Editorial ed = validarDatos();

						if (e != null) {
							try {
								controlador.insertarEditorial(ed);
							} catch (BBDDException e1) {
								JOptionPane.showConfirmDialog(contentPanel, e1.getMessage(),
										"Error insertando los datos", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	protected Editorial validarDatos() {
		Editorial ed = null;

		try {
			String nombre = txtNombre.getText();
			int anio = (int) spinnerAnio.getValue();

			ed = new Editorial(nombre, anio);
		} catch (NumberFormatException e) {
			JOptionPane.showConfirmDialog(contentPanel, "Debe introducir un número válido en código Editorial",
					"error en los datos", JOptionPane.ERROR_MESSAGE);
			return ed;
		}
		return ed;
	}

	public void setControlador(Controlador controlador) {
		this.controlador = controlador;

	}

}
