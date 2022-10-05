import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class Application extends JFrame implements actionEvent{

	static JPanel contentPane = new JPanel();
	static JScrollPane scroll =new JScrollPane();
	//private JPanel tablePane = new JPanel();
	private JTable table = new JTable();

	static String nomFichier;
	static HashMap<Integer,Long> addresseMap;
	static ArrayList<Livre> listeLivres = new ArrayList<>();
	static BufferedReader tmpReadTxt;
	static RandomAccessFile donnee;

	JComboBox cmbNumero =new JComboBox(getListeCBox("num"));
	JComboBox cmbCathegorie = new  JComboBox(getListeCBox("cathegorie"));

	static JButton btnLivres = new JButton("Tous les livres");
	static JButton btnModifierTitre = new JButton("Modifier les livres");
	static JButton btnSuprimer = new JButton("Suprimer des livres");
	static JButton btnAjouter = new JButton("Ajout des livres");

	//private DefaultTableModel model;
	static GridBagConstraints gbc_tlBar;
	
	/**

	 * Create the frame.
	 */
	public Application() {
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
		
		//scroll = remplirTable("", 0);
		//scroll.setVisible(false);
		//tablePane = remplirTable("", 0);
		//setScroll( remplirTable("", 0));
		String[] column = {"Numero","Titre","Numero Auteur","Annee","Nombre des pages","Cathegorie"};
		DefaultTableModel model = new DefaultTableModel(column,0);
		table.setModel(model);
		scroll = new JScrollPane(table);

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

		gbc_tlBar.gridwidth = 2;
		//gbc_t.insets = new Insets(0, 0, 5, 5);
		gbc_tlBar.fill = GridBagConstraints.BOTH;
		gbc_tlBar.gridx = 0;
		gbc_tlBar.gridy = 1;
		contentPane.add(scroll, gbc_tlBar);
		/*
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 2;
		gbc_table.insets = new Insets(0, 0, 5, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		contentPane.add(scroll, gbc_table);*/
	}
	public void actionBtn(ActionEvent ev){
		if(ev.getSource()== btnLivres){
			DefaultTableModel model = remplirTable("",0);
			table.setModel(model);
			
			//contentPane.repaint();
			
		}else if(ev.getSource()== btnModifierTitre){
			modifierLivre();

		}else if(ev.getSource()== btnSuprimer){
			Suprimer();

		}else if(ev.getSource()== btnAjouter){
//			ArrayList<String> data = new ArrayList<>(){{add(null);add(null);add(null);add(null);add(null);add(null);}};
			//paneString(data);
			ajouter();

		}
	}


	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()== cmbCathegorie){
			DefaultTableModel model = remplirTable((String)cmbCathegorie.getSelectedItem(),0);
			table.setModel(model);

		}else if(e.getSource()== cmbNumero){
			
			//scroll = remplirTable("",Integer.parseInt((String)cmbNumero.getSelectedItem()));
			DefaultTableModel model = remplirTable("",Integer.parseInt((String)cmbNumero.getSelectedItem()));
			table.setModel(model);
			

		}
//			contentPane.setVisible(true);
		}


	public DefaultTableModel remplirTable(String entree,int cle) {
		
		String[] column = {"Numero","Titre","Numero Auteur","Annee","Nombre des pages","Cathegorie"};
		DefaultTableModel model = new DefaultTableModel(column,0);

		if(cle==0){
			if(entree.equals("")){
				for(Livre livre:listeLivres){
					model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				
				}
			}else{
				for(Livre livre:listeLivres){
					String str = livre.getCathegorie();
					if(entree.equals(str)){
						model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				

					}
				}
			}

		}else{
			for(Livre livre:listeLivres){
				int num = livre.getNum();
				if(cle ==num){
					model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				

				}
			}

		}
		//JTable table = new JTable(model);
		//JPanel pane = new JPanel();
		//pane.add(table);
		//JScrollPane scroll = new JScrollPane(table);
		return model;

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

	public  String[] getListeCBox(String choix){
		String[] retour =new String[1];
		File file = new File("src\\livres.bin");
		if(!file.exists()){
		//chargerLivres();
		}else{

		int num=0;
		String  titre = "";
		int auteur = 0;
		int annee = 0;
		int pages = 0;
		String cathegorie="";
		ArrayList<String>  liste = new ArrayList<>();
		ArrayList<String>  listeTmp = new ArrayList<>();
		
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
		String nomFichier="";
		final JFileChooser fc = new JFileChooser();

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
							listeLivres.add(livre);
					}
					donnee.close();
				}else{
					int val_retour = fc.showOpenDialog(this);
					if (val_retour == JFileChooser.APPROVE_OPTION) {
						nomFichier= fc.getSelectedFile().getAbsolutePath();
					 } 
			
					tmpReadTxt = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(nomFichier), "UTF8"));
					//tmpReadTxt = new BufferedReader(new FileReader(fichierTxt));//"src\\livres.txt"
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
						listeLivres.add(livre);
					
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
		for(Livre livre:listeLivres){
			if(livre.getNum()==cle){
				listeLivres.remove(livre);
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
					for (Livre livre:listeLivres){
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
		for(Livre livre:listeLivres){
			if(cle==livre.getNum()){
				return true;
			}
		}
		return false;
	}
	public String[] paneString(ArrayList<String> data) {
		String[] retour = new String[6];
//		if(data.get(0)==null){
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
			//}
		
		
		return retour;        
	
	}
	public void ajouter() {
		String strCle = JOptionPane.showInputDialog(null, "Entrez le numero du livre a ajouter");
		int cle= Integer.parseInt(strCle);
	   	if(rechercheCle(cle)){
				JOptionPane.showMessageDialog(null, "le livre du numero "+ cle +"  existe deja!!");
				
		}else{
			ArrayList<String> data = new ArrayList<>(){{add(strCle);add(null);add(null);add(null);add(null);add(null);}};
			String[] retour = paneString(data);
			listeLivres.add(new Livre(Integer.parseInt(retour[0]),retour[1],Integer.parseInt(retour[2]),
				Integer.parseInt(retour[3]),Integer.parseInt(retour[4]),retour[5]));
			sauvgarder();
			DefaultComboBoxModel model = new DefaultComboBoxModel<>(getListeCBox("num"));
			cmbNumero.removeAll();
			cmbNumero.setModel(model);
		}

	}
	public void modifierLivre() {
		String strCle = JOptionPane.showInputDialog(null, "Entrez le numero du livre a modifier");
		int cle= Integer.parseInt(strCle);
	   	if(!rechercheCle(cle)){
				JOptionPane.showMessageDialog(null, "le livre du numero "+ cle +"n' existe pas!!");
				
		}else{
			for(Livre livre:listeLivres){
				if(livre.getNum()==cle){
					ArrayList<String> data = new ArrayList<>(){{
						add(strCle);add(livre.getTitre());
						add(String.valueOf(livre.getAuteur()));add(String.valueOf(livre.getAnnee()));
						add(String.valueOf(livre.getPages()));add(livre.getCathegorie());}};
					String[] retour = paneString(data);
					livre.setNum(cle);
					livre.setTitre(retour[1]);
					livre.setAuteur(Integer.parseInt(retour[2]));
					livre.setAnnee(Integer.parseInt(retour[3]));
					livre.setPages(Integer.parseInt(retour[4]));
					livre.setCathegorie(retour[5]);
					sauvgarder();
					DefaultComboBoxModel model = new DefaultComboBoxModel<>(getListeCBox("num"));
					cmbNumero.removeAll();
					cmbNumero.setModel(model);
					break;
				}

			}	
		}

		
	}
	 public static void main(String[] args) {
		Application frame = new Application();
		frame.setVisible(true);

/*		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application frame = new Application();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
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
