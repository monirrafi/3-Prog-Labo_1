import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class frmPrincipal extends JFrame implements actionEvent{

	static JPanel contentPane = new JPanel();
	static JTable table;
	private JScrollPane scroll;
	static HashMap<Integer,Long> addresseMap;
	static ArrayList<Livre> listeLivires = new ArrayList<>();
	static BufferedReader tmpReadTxt;
	static RandomAccessFile donnee;
	static JComboBox cmbNumero =new JComboBox(getListeCBox("num"));
	static JComboBox cmbCathegorie = new JComboBox(getListeCBox("cathegorie"));
	
	static JButton btnLivres = new JButton("Tous les livres");
	static JButton btnModifierTitre = new JButton("Modifier les livres");
	static JButton btnSuprimer = new JButton("Suprimer des livres");
	static JButton btnAjouter = new JButton("Ajout des livres");

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
		contentPane = new JPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 500);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{266, 62, 0};
		gbl_contentPane.rowHeights = new int[]{21, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		scroll = new JScrollPane();

		cmbNumero =new JComboBox(getListeCBox("num"));
		cmbCathegorie = new JComboBox(getListeCBox("cathegorie"));
		btnLivres = new JButton("Tous les livres");		
		
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
		JLabel lblCath = new JLabel("Choisissez votre Cathegorie");
		tlBar.add(lblCath);
		tlBar.add(cmbCathegorie);
		JLabel lblNumero = new JLabel("Choisissez votre Numero");
		tlBar.add(lblNumero);
		tlBar.add(cmbNumero);
		tlBar.add(btnModifierTitre);
		tlBar.add(btnAjouter);
		tlBar.add(btnSuprimer);

		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 2;
		gbc_table.insets = new Insets(0, 0, 5, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		contentPane.add(scroll, gbc_table);
	}
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()== cmbCathegorie){
			scroll = remplirTable((String)cmbCathegorie.getSelectedItem(),0);
				JOptionPane.showMessageDialog(null, scroll);
//			affichage();

		}else if(e.getSource()== cmbNumero){
			scroll = remplirTable("",Integer.parseInt((String)cmbNumero.getSelectedItem()));
			JOptionPane.showMessageDialog(null, scroll);
		}
			contentPane.setVisible(true);
		}


	public JScrollPane remplirTable(String entree,int cle) {
		
		String[] column = {"Numero","Titre","Numero Auteur","Annee","Nombre des pages","Cathegorie"};
		DefaultTableModel model = new DefaultTableModel(column,0);

		if(cle==0){
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

		}else{
			for(Livre livre:listeLivires){
				int num = livre.getNum();
				if(cle ==num){
					model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				

				}
			}

		}
		table = new JTable(model);
		JScrollPane scroll = new JScrollPane(table);
		return scroll;

	}
	public void actionBtn(ActionEvent ev){
		if(ev.getSource()== btnLivres){
			scroll = remplirTable("",0);
			JOptionPane.showMessageDialog(null, scroll);
			contentPane.repaint();

		}else if(ev.getSource()== btnModifierTitre){

		}else if(ev.getSource()== btnSuprimer){
			Suprimer();

		}else if(ev.getSource()== btnAjouter){
			ArrayList<String> data = new ArrayList<>(){{add(null);add(null);add(null);add(null);add(null);add(null);}};
			paneString(data);

		}
	}

	@Override
	public void action() {
		cmbCathegorie.addItemListener(this::itemStateChanged);
		cmbNumero.addItemListener(this::itemStateChanged);
		btnLivres.addActionListener(this::actionBtn);
		btnAjouter.addActionListener(this::actionBtn);
		btnModifierTitre.addActionListener(this::actionBtn);
		btnSuprimer.addActionListener(this::actionBtn);
		
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
	public void Suprimer() {
		int cle = Integer.parseInt(JOptionPane.showInputDialog(null, "Entrez le numero du livre a suprimer :"));
		for(Livre livre:listeLivires){
			if(livre.getNum()==cle){
				listeLivires.remove(livre);
				break;
			}
		}
		sauvgarder();
		DefaultComboBoxModel model = new DefaultComboBoxModel<>(getListeCBox("num"));
		cmbNumero.removeAll();
		cmbNumero.setModel(model);
		
	}
	public void sauvgarder() {
		addresseMap = new HashMap<>();
		int cle = 0;
		String  titre = "";
		int auteur = 0;
		int annee = 0;
		int pages = 0;
		String cathegorie = "";
		try {
					donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
					donnee.seek(0);
					for (Livre livre:listeLivires){
						cle = livre.getNum();
						titre =livre.getTitre();
						auteur = livre.getAuteur();
						annee = livre.getAnnee();
						pages = livre.getPages();
						cathegorie =  livre.getCathegorie();
						Long lng =donnee.getFilePointer();
						//400;Une aventure d'Astérix le gaulois. Le devin;11;1972;48;bandes dessinées 4+30+4+4+4+20=96
						addresseMap.put(cle,lng);
						donnee.writeInt(cle);
						donnee.writeUTF(titre);
						donnee.writeInt(auteur);
						donnee.writeInt(annee);
						donnee.writeInt(pages);
						donnee.writeUTF(cathegorie);
					}
					donnee.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
	}
	public boolean rechercheCle(int cle) {
		for(Livre livre:listeLivires){
			if(cle==livre.getNum()){
				return true;
			}
		}
		return false;
	}
	public String[] paneString(ArrayList<String> data) {
		String[] retour = new String[6];
		if(data.get(0).equals(null)){
		int cle= Integer.parseInt(JOptionPane.showInputDialog(null, "Entrez le cle a ajouter"));
	   	if(rechercheCle(cle)){
				JOptionPane.showMessageDialog(null, "le cle existe");
				
			}else{

		

				Dimension d =new Dimension(150,20);
				ArrayList<JTextField> listeJtxt = new ArrayList<>();
				ArrayList<String> listeChamps= new ArrayList<String>();
				listeChamps = new ArrayList<String>(){{add("Numero");add("Titre");add("Auteur");add("Annee");add("Pages");add("Cathegorie");}};
				
				JPanel gPane = new JPanel(new GridLayout(listeChamps.size(),1));
				
				for(int i=0;i<listeChamps.size();i++){
					JPanel pane = new JPanel();
					JTextField jtxt = new JTextField(data.get(i));
					jtxt.setPreferredSize(d);
	
					JLabel lbl = new JLabel(listeChamps.get(i));
					lbl.setPreferredSize(d);
					lbl.setLabelFor(jtxt);
					listeJtxt.add(jtxt);
					pane.add(lbl);
					pane.add(jtxt);
					gPane.add(pane);

				}
				int res = JOptionPane.showConfirmDialog(null,gPane);
				if(res == JOptionPane.YES_OPTION){
					for(int i=0;i<listeJtxt.size();i++){
						retour[i]= listeJtxt.get(i).getText();
					}
				} 
			}
		}
		
		return retour;        
	
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
	public JScrollPane getScroll() {
		return scroll;
	}
	public void setScroll(JScrollPane scroll) {
		this.scroll = scroll;
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
