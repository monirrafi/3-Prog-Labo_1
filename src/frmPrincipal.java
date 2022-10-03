import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JToolBar;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class frmPrincipal extends JFrame implements actionEvent{

	private JPanel contentPane = new JPanel();
	private JTable table;
	private JScrollPane scroll;
	private HashMap<Integer,Long> addresseMap;
	private ArrayList<Livre> listeLivires = new ArrayList<>();
	static BufferedReader tmpReadTxt;
	static RandomAccessFile donnee;
	static JComboBox cmbNumero =new JComboBox(getListeCBox("num"));
	static JComboBox cmbCathegorie = new JComboBox(getListeCBox("cathegorie"));
	
	static JButton btnLivres = new JButton("Tous les livres");
	//private DefaultTableModel model;
	static GridBagConstraints gbc_tlBar;
	/**

	 * Create the frame.
	 */
	public frmPrincipal() {
		chargerLivres();
		affichage();
		action();
	
		
	}
	public void affichage() {
		//table = new JTable();
		//remplirTable("");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		cmbNumero =new JComboBox(getListeCBox("num"));
		cmbCathegorie = new JComboBox(getListeCBox("cathegorie"));
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{266, 62, 0};
		gbl_contentPane.rowHeights = new int[]{21, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		scroll = new JScrollPane(table);
		
		JToolBar tlBar = new JToolBar();
		tlBar.setToolTipText("Liste des livres");
		tlBar.setForeground(Color.BLACK);
		tlBar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tlBar.setBackground(Color.WHITE);
		gbc_tlBar = new GridBagConstraints();
		gbc_tlBar.insets = new Insets(0, 0, 5, 5);
		gbc_tlBar.anchor = GridBagConstraints.NORTHWEST;
		gbc_tlBar.gridx = 0;
		gbc_tlBar.gridy = 0;
		contentPane.add(tlBar, gbc_tlBar);
		
		tlBar.add(btnLivres);
		//JLabel lblCath = new JLabel("Choisissez votre Cathegorie");
		//tlBar.add(lblCath);
		tlBar.add(cmbCathegorie);
		//JLabel lblNumero = new JLabel("Choisissez votre Numero");
		//tlBar.add(lblNumero);
		tlBar.add(cmbNumero);

		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 2;
		gbc_table.insets = new Insets(0, 0, 5, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		contentPane.add(scroll, gbc_table);
	}
	public void itemStateChanged(ItemEvent e) {
		//contentPane.setVisible(false);
		if(e.getSource()== cmbCathegorie){
			//remplirTable("");
			//contentPane.setVisible(false);
			scroll = remplirTable((String)cmbCathegorie.getSelectedItem());
			//table = new JTable(model);
			//scroll = new JScrollPane(table);
			//contentPane.setVisible(true);
				JOptionPane.showMessageDialog(null, "Votre choix :" +(String)cmbCathegorie.getSelectedItem());
				//contentPane.repaint();
			affichage();
			contentPane.repaint();

		}else if(e.getSource()== cmbNumero){
			JOptionPane.showMessageDialog(null, "bravo");
			
			contentPane.repaint();

		}
			//contentPane.setVisible(true);
		}


	public JScrollPane remplirTable(String entree) {
		
		String[] column = {"Numero","Titre","Numero Auteur","Annee","Nombre des pages","Cathegorie"};
		DefaultTableModel model = new DefaultTableModel(column,0);
		table = new JTable(model);
		if(entree.equals("")){
			for(Livre livre:listeLivires){
				model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				
			}
		}else{
			for(Livre livre:listeLivires){
				String str = livre.getCathegorie();
				if(entree.equals(str)){
					model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				

				}
			}

		}
		//table = new JTable(model);
		
		JScrollPane scroll = new JScrollPane(table);
		return scroll;

	}
	public void actionBtn(ActionEvent ev){
		if(ev.getSource()== btnLivres){
			//JOptionPane.showMessageDialog(null, "bravo");
			scroll = remplirTable("");
			//table = new JTable(model);
			//scroll = new JScrollPane(table);
			affichage();
			
			contentPane.repaint();

		}
	}

	@Override
	public void action() {
		cmbCathegorie.addItemListener(this::itemStateChanged);
		cmbNumero.addItemListener(this::itemStateChanged);
		btnLivres.addActionListener(this::actionBtn);
		
	}

	/**
	 * Launch the application.
	 */

	public static String[] getListeCBox(String choix){

		int num=0;
		String  titre = "";
		int auteur = 0;
		int annee = 0;
		int pages = 0;
		String cathegorie="";
		ArrayList<String>  liste = new ArrayList<>();
		ArrayList<String>  listeTmp = new ArrayList<>();
		String[] retour =new String[1];
		
		try {
			donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
			donnee.seek(0);
			for (int i=0;i<donnee.length();i++){
				num = donnee.readInt();
				titre=donnee.readUTF();
				auteur=donnee.readInt();
				annee=donnee.readInt();
				pages=donnee.readInt();
				cathegorie=donnee.readUTF();
				if(choix.equals("cathegorie")) {
					liste.add(cathegorie);
				}else {
					liste.add(String.valueOf(num));
				}
			}
			donnee.close();
	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		listeTmp.add(liste.get(0));
		for(String current:liste){
			if(listeTmp.indexOf(current)==-1){
				listeTmp.add(current);
			}
		}
		retour = new String[listeTmp.size()];
		for(int i=0;i<listeTmp.size();i++){
			retour[i]=listeTmp.get(i);
		}

		return retour;
	}
	public HashMap<Integer, Long> getAddresseMap() {
		return addresseMap;
	}
	public void setAddresseMap(HashMap<Integer, Long> addresseMap) {
		this.addresseMap = addresseMap;
	}

	public void chargerLivres() {
		addresseMap = new HashMap<>();
		int cle = 0;
		String  titre = "";
		int auteur = 0;
		int annee = 0;
		int pages = 0;
		String cathegorie = "";
		try {
				File file = new File("src\\livres.bin");
				if(file.exists()){
					donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
					donnee.seek(0);
					for (int i=0;i<donnee.length();i++){
							long adr = donnee.getFilePointer();
							cle = donnee.readInt();
							titre=donnee.readUTF();
							auteur=donnee.readInt();
							annee=donnee.readInt();
							pages=donnee.readInt();
							cathegorie=donnee.readUTF();
							addresseMap.put(cle,adr); 
							Livre livre =new Livre(cle,titre,auteur,annee,pages,cathegorie);
							listeLivires.add(livre);
					}
					donnee.close();
				}else{
					tmpReadTxt = new BufferedReader(new FileReader("src\\livres.txt"));
				    donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
					String ligne = tmpReadTxt.readLine();
					String[] elemt = new String[6];
					donnee.seek(0);

					while(ligne != null){
						elemt = ligne.split(";");
						cle = Integer.parseInt(elemt[0]);
						titre =elemt[1];
						auteur = Integer.parseInt(elemt[2]);
						annee = Integer.parseInt(elemt[3]);
						pages = Integer.parseInt(elemt[4]);
						cathegorie =  elemt[5];
						Long lng =donnee.getFilePointer();
						//400;Une aventure d'Astérix le gaulois. Le devin;11;1972;48;bandes dessinées 4+30+4+4+4+20=96
						addresseMap.put(cle,lng);
						donnee.writeInt(cle);
						donnee.writeUTF(titre);
						donnee.writeInt(auteur);
						donnee.writeInt(annee);
						donnee.writeInt(pages);
						donnee.writeUTF(cathegorie);
						Livre livre =new Livre(cle,titre,auteur,annee,pages,cathegorie);
						listeLivires.add(livre);
					
						ligne = tmpReadTxt.readLine();
					}	

					donnee.close();	
					tmpReadTxt.close();	
					
					
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public Long rechercherAddresse(int cle) {
		long adr=-1;
		System.out.println(addresseMap.size());
		for(Integer key:addresseMap.keySet()){
			if(key==cle){
				adr=addresseMap.get(key);
				break;
			}
		} 
		return adr;
		
	}

	 public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmPrincipal frame = new frmPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}



}

/*		int num = 0;
		String  titre = "";
		int auteur = 0;
		int annee = 0;
		int pages = 0;
		String cathegorie = "";
		try {
			donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
				
				donnee.seek(0);
				for (Integer cle:addresseMap.keySet()){
								
					long adr = addresseMap.get(cle);
					donnee.seek(adr); 
					num=donnee.readInt();
					titre=donnee.readUTF();
					auteur=donnee.readInt();
					annee=donnee.readInt();
					pages=donnee.readInt();
					cathegorie=donnee.readUTF();
					if(entree.equals("")){
						model.addRow(new Object[]{String.valueOf(num),titre,String.valueOf(auteur),String.valueOf(annee),String.valueOf(pages),cathegorie});				
					}else if(cathegorie.equals(entree)){
						model.addRow(new Object[]{String.valueOf(num),titre,String.valueOf(auteur),String.valueOf(annee),String.valueOf(pages),cathegorie});				
					}	
				
			}	
			donnee.close();				
			
				
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
	/*
	public void afficher() {
		JTextArea retour = new JTextArea(20,95);
		retour.append("Numero\t"+ Livre.formatMot("Titre",100)+"\t\tNumero Auteur\tAnnee\tPages\tCathegorie\n");
		//String retour = "";
		int num = 0;
		String  titre = "";
		int auteur = 0;
		int annee = 0;
		int pages = 0;
		String cathegorie = "";

		
		try {
			donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
			for (Integer cle:addresseMap.keySet())
                            {
				long adr = addresseMap.get(cle);
                donnee.seek(adr); 
                num=donnee.readInt();
                titre=donnee.readUTF();
                auteur=donnee.readInt();
                annee=donnee.readInt();
                pages=donnee.readInt();
                cathegorie=donnee.readUTF();

				Livre livre = new Livre(num,titre,auteur,annee,pages,cathegorie);
				retour.append(livre.toString());
				}
							donnee.close();
						} catch (Exception e) {
							// TODO: handle exception
						}
						JScrollPane pane = new JScrollPane(retour);
						JOptionPane.showMessageDialog(null, pane);			
	}*/
