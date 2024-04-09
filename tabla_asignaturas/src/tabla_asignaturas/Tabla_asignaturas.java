package tabla_asignaturas;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tabla_asignaturas {

	private JFrame frame;
	private JTextField textFieldcod;
	private JTextField textFieldnombre;
	private JTextField textFieldhoras;

	Connection con;
	
	/**
	 * Launch the application.
	 * 
	 * 
	 * @author alesia
	 * @author j.manuel
	 * 
	 * 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tabla_asignaturas window = new Tabla_asignaturas();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tabla_asignaturas() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//creacion 
		
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(6, 225, 234));
		frame.setBackground(new Color(98, 160, 234));
		frame.setBounds(100, 100, 654, 477);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DefaultTableModel model = new DefaultTableModel();
		
		model.addColumn("Codas");
		model.addColumn("Nombre");
		model.addColumn("Horas");
		frame.getContentPane().setLayout(null);

		
		JTable table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 0, 573, 167);
		
		frame.getContentPane().add(scrollPane);
		
		JLabel cod = new JLabel("Codigo asignatura:");
		cod.setBounds(12, 246, 141, 15);
		frame.getContentPane().add(cod);
		
		JLabel nombre = new JLabel("Nombre:");
		nombre.setBounds(12, 304, 70, 15);
		frame.getContentPane().add(nombre);
		
		JLabel horas = new JLabel("Horas:");
		horas.setBounds(12, 375, 70, 15);
		frame.getContentPane().add(horas);
		
		textFieldcod = new JTextField();
		textFieldcod.setBounds(171, 244, 114, 19);
		frame.getContentPane().add(textFieldcod);
		textFieldcod.setColumns(10);
		
		textFieldnombre = new JTextField();
		textFieldnombre.setColumns(10);
		textFieldnombre.setBounds(171, 319, 114, 19);
		frame.getContentPane().add(textFieldnombre);
		
		textFieldhoras = new JTextField();
		textFieldhoras.setColumns(10);
		textFieldhoras.setBounds(171, 387, 114, 19);
		frame.getContentPane().add(textFieldhoras);

		JButton mostrar = new JButton("Mostrar");
		mostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					con=ConnectionSingleton.getConnection();
					model.setRowCount(0);
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM asignatura");
					while (rs.next()) {
					 Object[] row = new Object[4];
					 row[0] = rs.getInt("codas"); 
					 row[1] = rs.getString("nombre");
					 row[2] = rs.getInt("horas");
					 model.addRow(row);
					}
			 
					stmt.close();
					rs.close();
					con.close();

					
					} catch (SQLException e) {
						System.err.println(e.getMessage());
						 e.getErrorCode();
						 e.printStackTrace();
					}	
				
			}
		});
		mostrar.setBounds(428, 179, 117, 25);
		frame.getContentPane().add(mostrar);
		
		mostrar.setVisible(false);
		
		mostrar.doClick();

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int index = table.getSelectedRow();
				
				textFieldcod.setText(model.getValueAt(index, 0).toString());
				textFieldnombre.setText(model.getValueAt(index, 1).toString());
				textFieldhoras.setText(model.getValueAt(index, 2).toString());		
				
				textFieldcod.setEditable(false);
			}
		});
			
		JButton guardar = new JButton("Guardar");
		guardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
							
				try {
					
				con=ConnectionSingleton.getConnection();
				
				int codas = Integer.parseInt(textFieldcod.getText());
				String nombre = textFieldnombre.getText();
				int horas = Integer.parseInt(textFieldhoras.getText());
				
				PreparedStatement ins_ps=con.prepareStatement("INSERT INTO asignatura VALUES (?,?,?)");
				ins_ps.setInt(1,codas);
				ins_ps.setString(2, nombre);
				ins_ps.setInt(3, horas);		
				
				int rowInserted = ins_ps.executeUpdate();
				
				JOptionPane.showMessageDialog(frame,
						"          Instruccion exitosa\n"
						+"         Filas afectadas:"+rowInserted,
						"MENSAJITO",
						JOptionPane.PLAIN_MESSAGE);
				
				ins_ps.close();
				
				mostrar.doClick();
				
				}catch (SQLException e) {
					System.err.println(e.getMessage());
					 e.getErrorCode();
					 e.printStackTrace();
				}		
				
			}
		});
		guardar.setBounds(428, 241, 117, 25);
		frame.getContentPane().add(guardar);
		
		
		JButton actualizar = new JButton("Actualizar");
		actualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				try {
					
					con=ConnectionSingleton.getConnection();
					
					int codas = Integer.parseInt(textFieldcod.getText());
					String nombre = textFieldnombre.getText();
					int horas = Integer.parseInt(textFieldhoras.getText());
					
					PreparedStatement ins_ps=con.prepareStatement("update asignatura set nombre=? , horas=? where codas=?");
					ins_ps.setString(1, nombre);
					ins_ps.setInt(2, horas);	
					ins_ps.setInt(3,codas);	
					
					int rowInserted = ins_ps.executeUpdate();

					JOptionPane.showMessageDialog(frame,
							"          Instruccion exitosa\n"
							+"         Filas afectadas:"+rowInserted,
							"MENSAJITO",
							JOptionPane.PLAIN_MESSAGE);
					
					
					mostrar.doClick();
					
					ins_ps.close();
					con.close();
					
					}catch (SQLException e) {
						System.err.println(e.getMessage());
						 e.getErrorCode();
						 e.printStackTrace();
					}	
				
				
				textFieldcod.setEditable(true);

			}
		});
		actualizar.setBounds(428, 316, 117, 25);
		frame.getContentPane().add(actualizar);
		
		JButton borrar = new JButton("Borrar");
		borrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			try {
				con=ConnectionSingleton.getConnection();
				
				int codas = Integer.parseInt(textFieldcod.getText());
								
				PreparedStatement ins_ps=con.prepareStatement("DELETE FROM asignatura WHERE asignatura.codas=?");
				ins_ps.setInt(1, codas);

				int rowInserted = ins_ps.executeUpdate();
				
				JOptionPane.showMessageDialog(frame,
						"          Instruccion exitosa\n"
						+"         Filas afectadas:"+rowInserted,
						"MENSAJITO",
						JOptionPane.PLAIN_MESSAGE);
				
				ins_ps.close();
				con.close();
				mostrar.doClick();
				
			} catch (SQLException e) {
				
				JOptionPane.showMessageDialog(frame,
					"        Fallo en el borrado de datos, la operacion no ha sido posible\n"
					+"		"+e.getMessage(),
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			}	
				
			}
		});
		borrar.setBounds(428, 384, 117, 25);
		frame.getContentPane().add(borrar);
		
		
		
	}
}
