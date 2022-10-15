import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.io.*;
import java.text.Normalizer;
import java.util.*;
import java.awt.event.*;

public class Application extends JFrame implements actionEvent{

/*============================================================================================================= */
/*										Declaration																*/
/*============================================================================================================= */
static JPanel contentPane = new JPanel();
	static JScrollPane scroll =new JScrollPane();
	private JTable table = new JTable();

	static String nomFichier;
	static HashMap<Integer,TableIndex> addresseMap = new HashMap<>();
	//static ArrayList<Livre> listeLivres = new ArrayList<>();
	static BufferedReader tmpReadTxt;
	static RandomAccessFile donnee;
	static String noAuteur=""; 

	JComboBox<String> cmbNumero =new JComboBox<>();
	JComboBox<String> cmbCathegorie = new  JComboBox<>();
	JComboBox<String> cmbAuteur = new  JComboBox<>();
	JComboBox<String>   champAuteur = new  JComboBox<>();
	static JButton btnLivres = new JButton("Afficher les livres");
	static JButton btnModifierTitre = new JButton("Modifier un titre");
	static JButton btnSuprimer = new JButton("Suprimer un livre");
	static JButton btnAjouter = new JButton("Ajouter un livre");
	static JButton btnQuitter = new JButton("Quitter");
	static JLabel lblSize;
	static GridBagConstraints gbc_tlBar;
	static 	JTableHeader entete;

/*============================================================================================================= */
/*										Constructeurs																*/
/*============================================================================================================= */
	public Application() {
		chargerLivres();
		affichage();
		action();
		
	}
	public void affichage() {
		ImageIcon logo = new ImageIcon(getClass().getResource("\\images\\biblio.png"));
		setIconImage(logo.getImage());
		contentPane = new JPanel();
		setTitle("Gestion de la bibliotheque");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1550, 700);
		contentPane.setBorder(new EmptyBorder(5, 5, 1, 0));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{266, 62, 0};
		gbl_contentPane.rowHeights = new int[]{21, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		table.setModel(imageTable());
		scroll = new JScrollPane(table);
		cmbNumero =new JComboBox<>(getListeCBox("num"));
		cmbCathegorie = new  JComboBox<>(getListeCBox("cathegorie"));
		cmbAuteur = new  JComboBox<>(getListeCBox("auteur"));
	
	
		btnStyle(btnLivres);
		btnStyle(btnAjouter);
		btnStyle(btnSuprimer);
		btnStyle(btnModifierTitre);
		btnStyle(btnQuitter);
		
		JToolBar tlBar = new JToolBar();
		
		tlBar.setToolTipText("Liste des livres");
		tlBar.setForeground(Color.BLACK);
		tlBar.setFont(new Font("Serif", Font.PLAIN, 16));
		tlBar.setBackground(Color.WHITE);
		gbc_tlBar = new GridBagConstraints();
		gbc_tlBar.insets = new Insets(0, 5, 10, 5);
		gbc_tlBar.anchor = GridBagConstraints.NORTHWEST;
		gbc_tlBar.gridx = 0;
		gbc_tlBar.gridy = 0;
		contentPane.add(tlBar, gbc_tlBar);
		
		//JLabel lblCath = new JLabel("Cathegorie");
		cmbCathegorie.setBackground(new Color(0,128,0));
		cmbCathegorie.setForeground(Color.white);
		cmbCathegorie.setFont( new Font("Serif", Font.BOLD, 20));
		cmbCathegorie.setOpaque(true);
		//JLabel lblNumero = new JLabel("Numero");
		cmbNumero.setBackground(new Color(123,104,238));
		cmbNumero.setForeground(Color.yellow);
		cmbNumero.setFont( new Font("Serif", Font.BOLD, 20));
		cmbNumero.setOpaque(true);
	
		cmbAuteur.setBackground(new Color(255,140,0));
		cmbAuteur.setForeground(Color.BLACK);
		cmbAuteur.setFont( new Font("Serif", Font.BOLD, 20));
		cmbAuteur.setOpaque(true);

		lblSize = new JLabel(" Le nombre des livres est " + remplirArrayliste().size() + " ");
		lblSize.setFont( new Font("Serif", Font.BOLD, 16)); 
		lblSize.setSize(new Dimension(350,20));

		
		tlBar.add(btnLivres);
		tlBar.add(btnModifierTitre);
		tlBar.add(btnAjouter);
		tlBar.add(btnSuprimer);

		tlBar.add(cmbCathegorie);
		//tlBar.add(lblCath);
		tlBar.add(cmbNumero);
		tlBar.add(cmbAuteur);
		//tlBar.add(lblNumero);
		tlBar.add(lblSize);
		tlBar.add(btnQuitter);
		
		scroll.setBackground(new Color(128,128,128));
		gbc_tlBar.gridwidth = 2;
		gbc_tlBar.fill = GridBagConstraints.BOTH;
		gbc_tlBar.gridx = 0;
		gbc_tlBar.gridy = 1;
		contentPane.add(scroll, gbc_tlBar);
	}

/*============================================================================================================= */
/*										Ecouetuers																*/
/*============================================================================================================= */

	public void actionBtn(ActionEvent ev){
		if(ev.getSource()== btnLivres){
			//DefaultTableModel model = remplirTable("","0");
			table.setModel(remplirTable("","0"));
			

		}else if(ev.getSource()== btnModifierTitre){
			modifierLivre();

		}else if(ev.getSource()== btnSuprimer){
			Suprimer();

		}else if(ev.getSource()== btnAjouter){
			ajouter();

		}else if(ev.getSource()== btnQuitter){
			sauvgarder();
			System.exit(0);

		}
		styleTable(new Color(12,128,144), Color.white);
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()== cmbCathegorie){
			DefaultTableModel model = remplirTable((String)cmbCathegorie.getSelectedItem(),"0");
			table.setModel(model);
			styleTable(new Color(0, 128, 0), Color.white);

		}else if(e.getSource()== cmbNumero){
			DefaultTableModel model = remplirTable("",(String)cmbNumero.getSelectedItem());
			table.setModel(model);
			styleTable(new Color(123,104,238), Color.yellow);
			
		}else if(e.getSource()== cmbAuteur){
			DefaultTableModel model = remplirTable("",(String)cmbAuteur.getSelectedItem());
			table.setModel(model);
			styleTable(new Color(255,140,0), Color.BLACK);
			
		}
		/*else if(e.getSource()== champAuteur){
			String no = (String)champAuteur.getSelectedItem();
			noAuteur = (String)champAuteur.getSelectedItem();
			
		}*/
	}
	@Override
	public void action() {
		cmbCathegorie.addItemListener(this::itemStateChanged);
		cmbNumero.addItemListener(this::itemStateChanged);
		cmbAuteur.addItemListener(this::itemStateChanged);
		//champAuteur.addItemListener(this::itemStateChanged);

		btnLivres.addActionListener(this::actionBtn);
		btnAjouter.addActionListener(this::actionBtn);
		btnModifierTitre.addActionListener(this::actionBtn);
		btnSuprimer.addActionListener(this::actionBtn);
		btnQuitter.addActionListener(this::actionBtn);
		
	}

/*============================================================================================================= */
/*										S-A-R     															    */
/*============================================================================================================= */
public void Suprimer() {
	long adr=0;
	String titre="",titreVide="";
	int auteur=0;
	int annee=0;
	int pages=0;
	String cathegorie="",cathegorieVide="";
String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre à modifier");
	int cle= Integer.parseInt(strCle);
	//Livre livreSprimer = new Livre();

	if(!rechercheCle(cle)){
			JOptionPane.showMessageDialog(null, "le livre du numero "+ cle +" n' existe pas!!");
			
	}else{
/*		
		for(Livre livre:listeLivres){
			if(livre.getNum()==cle){
				livreSprimer = livre;
				listeLivres.remove(livre);
				break;
			}
		}*/
		try {
			donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
			adr = rechercherAddresse(cle);
			donnee.seek(adr);
			cle = donnee.readInt();
			titre =donnee.readUTF();
			auteur = donnee.readInt();
			annee = donnee.readInt();
			pages = donnee.readInt();
			cathegorie =donnee.readUTF();
			donnee.close();
		} catch (Exception e) {
			e.getMessage();
		}

		try{
			//Long[] adrInfo = {adr,tailleMot(titre, cathegorie),(long) 0};
			for(int i=0;i<titre.length();i++){
				titreVide +=" ";
			}
			for(int i=0;i<cathegorie.length();i++){
				cathegorieVide +=" ";
			}
			TableIndex adrInfo = new TableIndex(adr, tailleMot(titreVide, cathegorieVide), 0, new Livre(-1*cle, titreVide, auteur, annee, pages, cathegorieVide));
			addresseMap.put(-1*cle, adrInfo);
			addresseMap.remove(cle);
			donnee.seek(adr);
				donnee.writeInt(-1*cle);
				donnee.writeUTF(titreVide);
				donnee.writeInt(0);
				donnee.writeInt(0);
				donnee.writeInt(0);
				donnee.writeUTF(cathegorieVide);
			donnee.close();

			//Long[] adrInfo = {adr,tailleMot(livreSprimer.getTitre(), livreSprimer.getCathegorie()),(long) 0};
			//addresseMap.get(cle)[1]=tailleMot(livreSprimer.getTitre(), livreSprimer.getCathegorie());
			//addresseMap.get(cle)[2]=(long) 0;

				
	
		} catch (Exception e) {
			e.getMessage();
		}
		DefaultComboBoxModel modelAuteur = new DefaultComboBoxModel<>(getListeCBox("auteur"));
		cmbAuteur.removeAll();
		cmbAuteur.setModel(modelAuteur);

		DefaultComboBoxModel modelNum = new DefaultComboBoxModel<>(getListeCBox("num"));
		cmbNumero.removeAll();
		cmbNumero.setModel(modelNum);

		DefaultComboBoxModel modelCath = new DefaultComboBoxModel<>(getListeCBox("cathegorie"));
		cmbCathegorie.removeAll();
		cmbCathegorie.setModel(modelCath);
		lblSize.setText(" Le nombre des livres est " + remplirArrayliste().size() + " ");
		//sauvgarder();
		JOptionPane.showMessageDialog(null,"le livre du numero "+ cle + " est suprimer avec succès");
		DefaultTableModel modelTable = remplirTable("","0");
		table.setModel(modelTable);

	}
	
}
public void ajouter() {
	String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre a ajouter");
	int cle= Integer.parseInt(strCle);
	if(rechercheCle(cle)){
			JOptionPane.showMessageDialog(null, "le livre du numéro "+ cle +"  existe déjà!!");
			
	}else{
		ArrayList<String> data = new ArrayList<>(){{add(strCle);add(null);add(null);add(null);add(null);add(null);}};
		String[] retour = paneString(data,new ArrayList<String>(){{add("Numéro");add("Titre");add("Année");add("Pages");}},"                         Entrez les informations du votre nouveau livre");
		if (retour != null){
				try {
					donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
					long adrVide = rechercherAddresseVide(retour[1], retour[5]);
					if(adrVide == -1){
						adrVide= donnee.length();
	
					}else{
						Integer del=0;
						for (java.util.Map.Entry<Integer, TableIndex> entry : addresseMap.entrySet()) { 
							if (entry.getValue().getAdr()==adrVide) { 
								del = entry.getKey();
							} 
						}
						addresseMap.remove(del);
					}

					//Long[] adrInfo = {adrVide,tailleMot(retour[1], retour[5]),(long) 1};
					TableIndex adrInfo = new TableIndex(adrVide, tailleMot(retour[1], retour[5]), 1,
					new Livre(Integer.parseInt(retour[0]),retour[1],Integer.parseInt(retour[2]),
						Integer.parseInt(retour[3]),Integer.parseInt(retour[4]),retour[5]));
					addresseMap.put(Integer.parseInt(retour[0]), adrInfo);

					donnee.seek(adrVide);
					donnee.writeInt(Integer.parseInt(retour[0]));
					donnee.writeUTF(retour[1]);
					donnee.writeInt(Integer.parseInt(retour[4]));
					donnee.writeInt(Integer.parseInt(retour[2]));
					donnee.writeInt(Integer.parseInt(retour[3]));
					donnee.writeUTF(retour[5]);
					donnee.close();

			
				} catch (Exception e) {
					e.getMessage();
				}
			DefaultComboBoxModel modelAuteur = new DefaultComboBoxModel<>(getListeCBox("auteur"));
			cmbAuteur.removeAll();
			cmbAuteur.setModel(modelAuteur);

			DefaultComboBoxModel modelNum = new DefaultComboBoxModel<>(getListeCBox("num"));
			cmbNumero.removeAll();
			cmbNumero.setModel(modelNum);

			DefaultComboBoxModel modelCath = new DefaultComboBoxModel<>(getListeCBox("cathegorie"));
			cmbCathegorie.removeAll();
			cmbCathegorie.setModel(modelCath);
			lblSize.setText(" Le nombre des livres est " + remplirArrayliste().size() + " ");

			//sauvgarder();
			DefaultTableModel modelTable = remplirTable("",String.valueOf(cle));
			table.setModel(modelTable);
		}	
	}

}
public void modifierLivre() {
	ArrayList<Livre> listeLivres = remplirArrayliste();
	String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre a modifier");
	int cle= Integer.parseInt(strCle);
	if(!rechercheCle(cle)){
			JOptionPane.showMessageDialog(null, "le livre du numéro "+ cle +" n' existe pas!!");
			
	}else{
		try {
			donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
			Livre livre = addresseMap.get(cle).getLivre();
			ArrayList<String> data = new ArrayList<>(){{add(strCle);add(livre.getTitre());}};
			String[] retour = paneString(data, new ArrayList<String>(){{add("Numéro");add("Titre");}},"                          Modifier le titre");
			long adr; 
			if (retour != null){
					if(livre.getCathegorie().length()< retour[1].length()){
						adr = addresseMap.get(cle).getAdr();
					}else{
						adr = donnee.length();
					}

					livre.setNum(cle);
					livre.setTitre(retour[1]);
					addresseMap.get(cle).setAdr(adr);
					addresseMap.get(cle).setLivre(livre);
					addresseMap.get(cle).setTaille(tailleMot(retour[1], livre.getCathegorie()));

					donnee.seek(adr);
					donnee.writeInt(cle);
					donnee.writeUTF(retour[1]);
					donnee.writeInt(livre.getAuteur());
					donnee.writeInt(livre.getAnnee());
					donnee.writeInt(livre.getPages());
					donnee.writeUTF(livre.getCathegorie());
					donnee.close();

			}
		} catch (Exception e) {
			e.getMessage();
		}
		DefaultComboBoxModel modelAuteur = new DefaultComboBoxModel<>(getListeCBox("auteur"));
		cmbAuteur.removeAll();
		cmbAuteur.setModel(modelAuteur);

		DefaultComboBoxModel modelNum = new DefaultComboBoxModel<>(getListeCBox("num"));
		cmbNumero.removeAll();
		cmbNumero.setModel(modelNum);

		DefaultComboBoxModel modelCath = new DefaultComboBoxModel<>(getListeCBox("cathegorie"));
		cmbCathegorie.removeAll();
		cmbCathegorie.setModel(modelCath);
		lblSize.setText(" Le nombre des livres est " + listeLivres.size() + " ");

		//sauvgarder();
		DefaultTableModel modelTable = remplirTable("",String.valueOf(cle));
		table.setModel(modelTable);
	}
}

public boolean rechercheCle(int cle) {
	for(Integer key:addresseMap.keySet()){
		if(cle==key){
			return true;
		}
	}

	/*
	for(Livre livre:listeLivres){
		if(cle==livre.getNum()){
			return true;
		}
	}*/
	return false;
}
public String[] paneString(ArrayList<String> data,ArrayList<String> listeChamps,String titre) {
	String[] retour = new String[6];
//		if(data.get(0)==null){
			Dimension d =new Dimension(350,20);
			Color cl = new Color(102,178,255);
			ArrayList<JTextField> listeJtxt = new ArrayList<>();
			//ArrayList<String> listeChamps= new ArrayList<String>();
			//listeChamps = new ArrayList<String>(){{add("Numero");add("Titre");add("Auteur");add("Annee");add("Pages");}};

			JPanel panePrincipal = new JPanel(new GridBagLayout());
			JPanel gPane = new JPanel(new GridLayout(listeChamps.size()+2,1,0,5));
			GridBagConstraints c = new GridBagConstraints();	
			JLabel lblTitre = new JLabel(titre);
			lblTitre.setFont(new Font("Serif", Font.BOLD, 20));
//			entete.setBackground(new Color(128,128,128));//new Color(105,105,105));
			lblTitre.setForeground(Color.blue);
				ButtonGroup groupeWeb = new ButtonGroup();
				gPane.add(lblTitre);
				for(int i=0;i<listeChamps.size();i++){
					JPanel pane = new JPanel();
					JTextField jtxt = new JTextField(data.get(i));
					jtxt.setPreferredSize(d);
					JLabel lbl = new JLabel(listeChamps.get(i));
					lbl.setPreferredSize(new Dimension(50,20));
					lbl.setLabelFor(jtxt);
					listeJtxt.add(jtxt);
					pane.add(lbl);
					pane.add(jtxt);
					gPane.add(pane);
	
				}
				c.weightx = 0.0;
				c.gridx = 0;
				c.gridy = 0;
				c.gridwidth=1;
				panePrincipal.add(gPane,c);
				
				if(listeChamps.size()>2){	
		
				JLabel lblChoix = new JLabel("                    Choisissez une cathegorie ");
				JPanel paneRadio = new JPanel(new GridLayout(2,1,0,5));
				paneRadio.setBackground(cl);
				JPanel paneElementradio = new JPanel();
				paneElementradio.setBackground(cl);
				JRadioButton vide = new JRadioButton("");
				
				vide.setBackground(cl);
				groupeWeb.add(vide);
				vide.setSelected(true);
				paneElementradio.add(vide);
				String[] listeCathegorie = getListeCBox("cathegorie");
				int longueur = listeCathegorie.length;
				for(int i=1;i<longueur;i++){
					JRadioButton rBtn = new JRadioButton(listeCathegorie[i]);
					rBtn.setBackground(cl);
					groupeWeb.add(rBtn);
					paneElementradio.add(rBtn);
				}
				paneRadio.add(lblChoix);
				paneRadio.add(paneElementradio);
				paneRadio.setPreferredSize(new Dimension(300,10));
				c.ipadx = 150;      
				c.ipady = 50;      
				c.weightx = 0.0;
				c.gridx = 0;
				c.gridy = 1;
				c.gridwidth=2;
				panePrincipal.add(paneRadio,c);
				JPanel paneAuteur = new JPanel();
				champAuteur = new  JComboBox<>(getListeCBox("auteur"));
				champAuteur.setPreferredSize(d);
				champAuteur.addActionListener(new ActionListener() {     
					@Override
					public void actionPerformed(ActionEvent e) {
					   noAuteur = champAuteur.getSelectedItem().toString();      
					}
				  });
				JLabel lblAuteur = new JLabel("Auteur");
				lblAuteur.setPreferredSize(new Dimension(50,20));
				lblAuteur.setLabelFor(champAuteur);
				paneAuteur.add(lblAuteur);
				paneAuteur.add(champAuteur);
				gPane.add(paneAuteur);
				}	

	
			int res = JOptionPane.showConfirmDialog(null,panePrincipal,"Modification Livre",JOptionPane.YES_NO_CANCEL_OPTION);
			if(res == JOptionPane.YES_OPTION){
				for(int i=0;i<listeJtxt.size();i++){
					retour[i]= listeJtxt.get(i).getText();
				}
				if(noAuteur.equals("")){
					retour[listeJtxt.size()] = "0";

				}else{
					retour[listeJtxt.size()] = noAuteur;
				}
				Enumeration<AbstractButton> allRadioButton=groupeWeb.getElements();  
				while(allRadioButton.hasMoreElements())  
				{  
				   JRadioButton temp=(JRadioButton)allRadioButton.nextElement();  
				   if(temp.isSelected())  
				   { 
						retour[listeJtxt.size()+1]= temp.getText();  
				   }  
				}            
				
			}else{
				retour = null;
			}  
		//}
	
	
	return retour;        

}

/*============================================================================================================= */
/*										Fonctions																*/
/*============================================================================================================= */
public DefaultTableModel remplirTable(String entree,String strCle) {

		//ArrayList<Livre> listeLivres = remplirArrayliste();
		
		String[] column = {"Numero","Titre","Numero Auteur","Annee","Nombre des pages","Cathegorie"};
		DefaultTableModel model = new DefaultTableModel(column,0);
		if(entree.equals("Choisissez Cathegorie") || strCle.equals("Choisissez Auteur") || strCle.equals("Choisissez Livre") ){
		//	for(Livre livre:listeLivres){
			for(Integer key:addresseMap.keySet()){
				if(key>=0){
			 	//Livre livre = addresseMap.get(key).getLivre();
				model.addRow(new Object[]{key,addresseMap.get(key).getLivre().getTitre(),
					addresseMap.get(key).getLivre().getAuteur(),addresseMap.get(key).getLivre().getAnnee(),
					addresseMap.get(key).getLivre().getPages(),addresseMap.get(key).getLivre().getCathegorie()});				
				}
			}
		}else{
			int cle = Integer.parseInt(strCle);
			if(cle==0){
				if(entree.equals("")){
					for(Integer key:addresseMap.keySet()){
						if(key>=0){
							//Livre livre = addresseMap.get(key).getLivre();
					   model.addRow(new Object[]{key,addresseMap.get(key).getLivre().getTitre(),
						   addresseMap.get(key).getLivre().getAuteur(),addresseMap.get(key).getLivre().getAnnee(),
						   addresseMap.get(key).getLivre().getPages(),addresseMap.get(key).getLivre().getCathegorie()});				
						   }
					}
				}else{
					for(Integer key:addresseMap.keySet()){
						if(key>=0){
							//Livre livre = addresseMap.get(key).getLivre();
						String str = addresseMap.get(key).getLivre().getCathegorie();
						if(entree.equals(str)){
							   model.addRow(new Object[]{key,addresseMap.get(key).getLivre().getTitre(),
								   addresseMap.get(key).getLivre().getAuteur(),addresseMap.get(key).getLivre().getAnnee(),
								   addresseMap.get(key).getLivre().getPages(),addresseMap.get(key).getLivre().getCathegorie()});				
			   
						}
						}
					}
				}

			}else{
				for(Integer key:addresseMap.keySet()){
					if(key>=0){
						int num = addresseMap.get(key).getLivre().getNum();
					if(cle ==num){
						model.addRow(new Object[]{key,addresseMap.get(key).getLivre().getTitre(),
							addresseMap.get(key).getLivre().getAuteur(),addresseMap.get(key).getLivre().getAnnee(),
							addresseMap.get(key).getLivre().getPages(),addresseMap.get(key).getLivre().getCathegorie()});				

					}
					}
				}
				for(Integer key:addresseMap.keySet()){
					if(key>=0){
						int num = addresseMap.get(key).getLivre().getAuteur();
					if(cle ==num){
						model.addRow(new Object[]{key,addresseMap.get(key).getLivre().getTitre(),
							addresseMap.get(key).getLivre().getAuteur(),addresseMap.get(key).getLivre().getAnnee(),
							addresseMap.get(key).getLivre().getPages(),addresseMap.get(key).getLivre().getCathegorie()});				

					}
					}
				}
			}
		}
		return model;

	}

	public  String[] getListeCBox(String choix){

		//chargerLivres();
	
		String[] retour =new String[1];
		File file = new File("src\\livres.bin");
		if(file.exists()){

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
			//donnee.seek(0);
			//for (int i=0;i<donnee.length();i++){
			for(Integer key:addresseMap.keySet()){
				if(key >0){
  				donnee.seek(addresseMap.get(key).getAdr());
				num = donnee.readInt();
				titre=donnee.readUTF();
				auteur=donnee.readInt();
				annee=donnee.readInt();
				pages=donnee.readInt();
				cathegorie=donnee.readUTF();
					if(choix.equals("cathegorie")) {
						liste.add(cathegorie);
					}else if(choix.equals("auteur")) {
						liste.add(String.valueOf(auteur));
					}else  if(choix.equals("num")) {
						liste.add(String.valueOf(num));
					}
				}
			}
			donnee.close();
	
		} catch (Exception e) {
			e.getMessage();
		}
		//enlever les doublants
		listeTmp.add(liste.get(0));
		for(String current:liste){
			if(listeTmp.indexOf(current)==-1){
				listeTmp.add(current);
			}
		}
		//les premiers elements de la liste deroulante
		retour = new String[listeTmp.size()+1];
		if(choix.equals("cathegorie")) {
			retour[0]="Choisissez Cathegorie";
		}else if(choix.equals("auteur")) {
			retour[0]="Choisissez Auteur";
		}else if(choix.equals("num")) {
			retour[0]="Choisissez Livre";
		}
		//le tableau sans doublant
		for(int i=0;i<listeTmp.size();i++){
			retour[i+1]=listeTmp.get(i);
		}
	}


		return retour;
	
	}
	public void chargerLivres() {
		String nomFichier="",pathFichier="";
		final JFileChooser fc = new JFileChooser();

		//addresseMap = new HashMap<>();
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
							//Long[] infoAdr = {adr,tailleMot(titre, cathegorie),(long) 1};
							TableIndex infoAdr = new TableIndex(adr, tailleMot(titre, cathegorie), 1,
							new Livre(cle,titre,auteur,annee,pages,cathegorie));

							addresseMap.put(cle,infoAdr); 
								//Livre livre =new Livre(cle,titre,auteur,annee,pages,cathegorie);
								//listeLivres.add(livre);
							
					}
					donnee.close();
					

				}else{
					if(addresseMap.size()==0){
						int val_retour = fc.showOpenDialog(this);
					if (val_retour == JFileChooser.APPROVE_OPTION) {
						pathFichier= fc.getSelectedFile().getAbsolutePath();
						nomFichier= fc.getSelectedFile().getName();
					
						if(nomFichier.equals("livres.txt")){ 
					
							tmpReadTxt = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(pathFichier), "UTF8"));
							//tmpReadTxt = new BufferedReader(new FileReader(fichierTxt));//"src\\livres.txt"
							donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
							String ligne = tmpReadTxt.readLine();
							String[] elemt = new String[6];
							donnee.seek(0);
							long debut=0;
							while(ligne != null){
								elemt = ligne.split(";");
								cle = Integer.parseInt(elemt[0]);
								titre =elemt[1];
								auteur = Integer.parseInt(elemt[2]);
								annee = Integer.parseInt(elemt[3]);
								pages = Integer.parseInt(elemt[4]);
								cathegorie =  elemt[5];

								long adr = donnee.getFilePointer();
								//Long[] infoAdr = {adr,adr-debut,(long) 1};
								TableIndex infoAdr = new TableIndex(adr, tailleMot(titre, cathegorie), 1,
								new Livre(cle,titre,auteur,annee,pages,cathegorie));
								debut =adr;

								addresseMap.put(cle,infoAdr);
								donnee.writeInt(cle);
								donnee.writeUTF(titre);
								donnee.writeInt(auteur);
								donnee.writeInt(annee);
								donnee.writeInt(pages);
								donnee.writeUTF(cathegorie);
								//Livre livre =new Livre(cle,titre,auteur,annee,pages,cathegorie);
								//listeLivres.add(livre);
							
								ligne = tmpReadTxt.readLine();

							}	

							donnee.close();	
							tmpReadTxt.close();	
						}
							
							
						}else{
							JOptionPane.showMessageDialog(null, "Le nom du fichier doit etre << livres.txt >>!!");
							System.exit(0);
						}
					}
				}
		} catch (Exception e) {
			e.getMessage();
		}
		
		
	}

	public static ArrayList<Livre> remplirArrayliste() {
		ArrayList<Livre> listeLivres = new ArrayList<Livre>();
		File file = new File("src\\livres.bin");
		if(file.exists()){

		int num=0;
		String  titre = "";
		int auteur = 0;
		int annee = 0;
		int pages = 0;
		String cathegorie="";
	
		try {
			donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
			//donnee.seek(0);
			//for (int i=0;i<donnee.length();i++){
			for(Integer key:addresseMap.keySet()){
				if(key>0){
				donnee.seek(addresseMap.get(key).getAdr());
				num = donnee.readInt();
				titre=donnee.readUTF();
				auteur=donnee.readInt();
				annee=donnee.readInt();
				pages=donnee.readInt();
				cathegorie=donnee.readUTF();
				 listeLivres.add(new Livre(num,titre,auteur,annee,pages,cathegorie));
				}
				
			}
			donnee.close();
	
		} catch (Exception e) {
			e.getMessage();
		}
		
	}

	return listeLivres;
		
}

public DefaultTableModel imageTable() {
	entete = table.getTableHeader();
	entete.setFont(new Font("Serif", Font.BOLD, 20));
	entete.setBackground(Color.orange);//new Color(128,128,128));//new Color(105,105,105));
	entete.setForeground(Color.BLACK);

	String[] column = {"Bienvenue à l'application de gestion des livres "};
	table.setRowHeight(558);
	DefaultTableModel model = new DefaultTableModel(column,0)
	{
		
		public Class getColumnClass(int column)
		{
			switch (column)
			{
				case 0: return Icon.class;
				default: return super.getColumnClass(column);
			}
		}
	};

	ImageIcon img =  new ImageIcon("src\\images\\livre2.jpg");
	model.addRow(new Object[]{img});

	return model;	
}
public void btnStyle(JButton btn){
	btn.setSize(new Dimension(200,20));
	btn.setBackground(new Color(12,128,144));		
	btn.setForeground(Color.white);
	btn.setFont( new Font("Serif", Font.BOLD, 18));
	btn.setOpaque(true);

}

public void styleTable(Color bgColor,Color pColor) {
	JTableHeader entete = table.getTableHeader();
	entete.setFont(new Font("Serif", Font.BOLD, 18));
	entete.setBackground(new Color(128,128,128));//new Color(105,105,105));
	entete.setForeground(Color.white);
	TableColumnModel columnModelEntete = entete.getColumnModel();
	columnModelEntete.getColumn(0).setPreferredWidth(5);
	columnModelEntete.getColumn(1).setPreferredWidth(400);
	columnModelEntete.getColumn(2).setPreferredWidth(5);
	columnModelEntete.getColumn(3).setPreferredWidth(5);
	columnModelEntete.getColumn(4).setPreferredWidth(5);
	columnModelEntete.getColumn(5).setPreferredWidth(100);
	TableColumnModel columnModel = table.getColumnModel();
	columnModel.getColumn(0).setPreferredWidth(5);
	columnModel.getColumn(1).setPreferredWidth(400);
	columnModel.getColumn(2).setPreferredWidth(5);
	columnModel.getColumn(3).setPreferredWidth(5);
	columnModel.getColumn(4).setPreferredWidth(5);
	columnModel.getColumn(5).setPreferredWidth(100);
	table.setBackground(bgColor);
	table.setForeground(pColor);
	table.setRowHeight(20);
	table.setFont(new Font("Serif", Font.BOLD, 18));
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}


public void sauvgarder() {
		//ArrayList<Livre> listeLivres = remplirArrayliste();
		//addresseMap = new HashMap<>();
		int cle = 0;
		String  titre = "";
		int auteur = 0;
		int annee = 0;
		int pages = 0;
		String cathegorie = "";
		try {
					donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
					donnee.seek(0);
					//long debut=0;
					for (Integer key:addresseMap.keySet()){
						cle = key;
						titre =addresseMap.get(key).getLivre().getTitre();
						auteur = addresseMap.get(key).getLivre().getAuteur();
						annee = addresseMap.get(key).getLivre().getAnnee();
						pages = addresseMap.get(key).getLivre().getPages();
						cathegorie =  addresseMap.get(key).getLivre().getCathegorie();
						//Long lng =donnee.getFilePointer();
						
						//long adr = donnee.getFilePointer();
						//Long[] infoAdr = {adr,adr-debut,(long) 1};
						//TableIndex infoAdr = new TableIndex(adr, tailleMot(titre, cathegorie), 1,
						//new Livre(cle,titre,auteur,annee,pages,cathegorie));

						//debut =adr;

						//addresseMap.put(cle,infoAdr);
						donnee.writeInt(cle);
						donnee.writeUTF(titre);
						donnee.writeInt(auteur);
						donnee.writeInt(annee);
						donnee.writeInt(pages);
						donnee.writeUTF(cathegorie);
					}
					donnee.close();
				} catch (Exception e) {
					e.getMessage();
				}
				/*
				DefaultComboBoxModel modelAuteur = new DefaultComboBoxModel<>(getListeCBox("auteur"));
				cmbAuteur.removeAll();
				cmbAuteur.setModel(modelAuteur);

				DefaultComboBoxModel modelNum = new DefaultComboBoxModel<>(getListeCBox("num"));
				cmbNumero.removeAll();
				cmbNumero.setModel(modelNum);

				DefaultComboBoxModel modelCath = new DefaultComboBoxModel<>(getListeCBox("cathegorie"));
				cmbCathegorie.removeAll();
				cmbCathegorie.setModel(modelCath);
				lblSize.setText(" Le nombre des livres est " + listeLivres.size() + " ");*/
								

	
}

/*============================================================================================================= */
/*										geters et seters																*/
/*============================================================================================================= */

public HashMap<Integer, TableIndex> getAddresseMap() {
		return addresseMap;
	}
	public void setAddresseMap(HashMap<Integer, TableIndex> addresseMap) {
		this.addresseMap = addresseMap;
	}

	public Long rechercherAddresse(int cle) {
		long adr=-1;
		for(Integer key:addresseMap.keySet()){
			if(key==cle){
				adr=addresseMap.get(key).getAdr();
				break;
			}
		} 
		return adr;
		
	}
	public long rechercherAddresseVide(String titre, String cathegorie) {
		long adr=-1;
		for(TableIndex val:addresseMap.values()){
			//System.out.println(val[0] + " " + val[2] + " " + val[1] + " " + tailleMot(titre, cathegorie));
			//System.out.println(-1*cle + " " + addresseMap.get(-1*cle)[0] + " " + addresseMap.get(-1*cle)[1] + " " + addresseMap.get(-1*cle)[2]);
			if(val.getStatus()==0 && val.getAdr()>= tailleMot(titre, cathegorie)){
				adr=val.getAdr();
				break;
			}
		} 
		return adr;
		
	}
	public static boolean avecAccent(String chaine){
    String strTemp = Normalizer.normalize(chaine, Normalizer.Form.NFD);
    if(chaine.equals(strTemp)){
      return false;
    }else{
      return true;
    }
    }
	public long tailleMot(String titre,String cathegorie) {
		long taille = titre.length() + cathegorie.length() + 20;
		if(avecAccent(titre) && avecAccent(cathegorie)){
			taille+=2;
		}else if(avecAccent(titre) || avecAccent(cathegorie)){
			taille+=1;
		}
		return taille;
	}

	 public static void main(String[] args) {
		Application frame = new Application();
		frame.setVisible(true);
		//remplirArrayliste();
	 }	


}

