import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.io.*;
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
	static HashMap<Integer,Long> addresseMap;
	static ArrayList<Livre> listeLivres = new ArrayList<>();
	static BufferedReader tmpReadTxt;
	static RandomAccessFile donnee;

	JComboBox<String> cmbNumero =new JComboBox<>(getListeCBox("num"));
	JComboBox<String> cmbCathegorie = new  JComboBox<>(getListeCBox("cathegorie"));
	JComboBox<String> cmbAuteur = new  JComboBox<>(getListeCBox("auteur"));

	static JButton btnLivres = new JButton("Afficher les livres");
	static JButton btnModifierTitre = new JButton("Modifier le titre");
	static JButton btnSuprimer = new JButton("Suprimer un livre");
	static JButton btnAjouter = new JButton("Ajouter un livre");
	static JButton btnQuitter = new JButton("Quitter");
	static JLabel lblSize;
		
	//private DefaultTableModel model;
	static GridBagConstraints gbc_tlBar;

/*============================================================================================================= */
/*										Constructeurs																*/
/*============================================================================================================= */
public Application() {
		chargerLivres();
		affichage();
		action();
		
	}
	public void affichage() {
		contentPane = new JPanel();
		setTitle("Gestion des livres");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10, 10, 1500, 700);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 0));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{266, 62, 0};
		gbl_contentPane.rowHeights = new int[]{21, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		JTableHeader entete = table.getTableHeader();
		entete.setFont(new Font("Serif", Font.BOLD, 20));
		entete.setBackground(new Color(128,128,128));//new Color(105,105,105));
		entete.setForeground(Color.white);
		
		String[] column = {"Numero","Titre","Numero Auteur","Annee","Nombre des pages","Cathegorie"};
		DefaultTableModel model = new DefaultTableModel(column,0);
		table.setModel(model);
		//scroll = new JScrollPane(table);
		ImageIcon img = new ImageIcon("src\\livre1.png");

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
		tlBar.setFont(new Font("Tahoma", Font.PLAIN, 16));
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

		lblSize = new JLabel(" Le nombre des livres est " + listeLivres.size() + " ");
		lblSize.setFont( new Font("Serif", Font.BOLD, 20)); 
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
			DefaultTableModel model = remplirTable("","0");
			table.setModel(model);
		

		}else if(ev.getSource()== btnModifierTitre){
			modifierLivre();

		}else if(ev.getSource()== btnSuprimer){
			Suprimer();

		}else if(ev.getSource()== btnAjouter){
			ajouter();

		}else if(ev.getSource()== btnQuitter){
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
	}
	@Override
	public void action() {
		cmbCathegorie.addItemListener(this::itemStateChanged);
		cmbNumero.addItemListener(this::itemStateChanged);
		cmbAuteur.addItemListener(this::itemStateChanged);
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
	String strCle = JOptionPane.showInputDialog(null, "Entrez le numero du livre a modifier");
	int cle= Integer.parseInt(strCle);
	   if(!rechercheCle(cle)){
			JOptionPane.showMessageDialog(null, "le livre du numero "+ cle +"n' existe pas!!");
			
	}else{
		
		for(Livre livre:listeLivres){
			if(livre.getNum()==cle){
				listeLivres.remove(livre);
				break;
			}
		}
		try {
			donnee = new RandomAccessFile(new File("src\\livres.bin"), "rw");
			donnee.seek(rechercherAddresse(cle));
				donnee.readInt();
				donnee.writeInt(-1);
				donnee.writeUTF("");
				donnee.writeInt(0);
				donnee.writeInt(0);
				donnee.writeInt(0);
				donnee.writeUTF("");
			donnee.close();
	
		} catch (Exception e) {
			e.getMessage();
		}

		sauvgarder();
		JOptionPane.showMessageDialog(null,"le livre du numero "+ cle + " est suprimer avec succes");
		DefaultTableModel modelTable = remplirTable("","0");
		table.setModel(modelTable);
	}
	
}
public void ajouter() {
	String strCle = JOptionPane.showInputDialog(null, "Entrez le numero du livre a ajouter");
	int cle= Integer.parseInt(strCle);
	if(rechercheCle(cle)){
			JOptionPane.showMessageDialog(null, "le livre du numero "+ cle +"  existe deja!!");
			
	}else{
		ArrayList<String> data = new ArrayList<>(){{add(strCle);add(null);add(null);add(null);add(null);add(null);}};
		String[] retour = paneString(data,new ArrayList<String>(){{add("Numero");add("Titre");add("Auteur");add("Annee");add("Pages");}},"                         Entrez les informations du votre nouveau livre");
		if (retour != null){
			listeLivres.add(new Livre(Integer.parseInt(retour[0]),retour[1],Integer.parseInt(retour[2]),
				Integer.parseInt(retour[3]),Integer.parseInt(retour[4]),retour[5]));

			sauvgarder();
			DefaultTableModel modelTable = remplirTable("",String.valueOf(cle));
			table.setModel(modelTable);
		}	
	}

}
public void modifierLivre() {
	String strCle = JOptionPane.showInputDialog(null, "Entrez le numero du livre a modifier");
	int cle= Integer.parseInt(strCle);
	   if(!rechercheCle(cle)){
			JOptionPane.showMessageDialog(null, "le livre du numero "+ cle +" n' existe pas!!");
			
	}else{
		for(Livre livre:listeLivres){
			if(livre.getNum()==cle){
				ArrayList<String> data = new ArrayList<>(){{
					add(strCle);add(livre.getTitre());}};
//					add(String.valueOf(livre.getAuteur()));add(String.valueOf(livre.getAnnee()));
//					add(String.valueOf(livre.getPages()));add(livre.getCathegorie());
				String[] retour = paneString(data, new ArrayList<String>(){{add("Numero");add("Titre");}},"                          Modifier le titre");
				if (retour != null){
					livre.setNum(cle);
					livre.setTitre(retour[1]);
//					livre.setAuteur(Integer.parseInt(retour[2]));
//					livre.setAnnee(Integer.parseInt(retour[3]));
//					livre.setPages(Integer.parseInt(retour[4]));
//					livre.setCathegorie(retour[5]);
					break;
				}
			}

		}
		
		sauvgarder();
		DefaultTableModel modelTable = remplirTable("",String.valueOf(cle));
		table.setModel(modelTable);
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
public String[] paneString(ArrayList<String> data,ArrayList<String> listeChamps,String titre) {
	String[] retour = new String[6];
//		if(data.get(0)==null){
			Dimension d =new Dimension(350,20);
			Color cl = new Color(102,178,255);
			ArrayList<JTextField> listeJtxt = new ArrayList<>();
			//ArrayList<String> listeChamps= new ArrayList<String>();
			//listeChamps = new ArrayList<String>(){{add("Numero");add("Titre");add("Auteur");add("Annee");add("Pages");}};

			JPanel panePrincipal = new JPanel(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();	
			JLabel lblTitre = new JLabel(titre);
			lblTitre.setFont(new Font("Serif", Font.BOLD, 20));
//			entete.setBackground(new Color(128,128,128));//new Color(105,105,105));
			lblTitre.setForeground(Color.blue);
				ButtonGroup groupeWeb = new ButtonGroup();
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
			}	

			JPanel gPane = new JPanel(new GridLayout(listeChamps.size()+1,1,0,5));
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
	
			int res = JOptionPane.showConfirmDialog(null,panePrincipal,"Modification Livre",JOptionPane.YES_NO_CANCEL_OPTION);
			if(res == JOptionPane.YES_OPTION){
				for(int i=0;i<listeJtxt.size();i++){
					retour[i]= listeJtxt.get(i).getText();
				}
				Enumeration<AbstractButton> allRadioButton=groupeWeb.getElements();  
				while(allRadioButton.hasMoreElements())  
				{  
				   JRadioButton temp=(JRadioButton)allRadioButton.nextElement();  
				   if(temp.isSelected())  
				   { 
						retour[listeJtxt.size()]= temp.getText();  
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

	public void btnStyle(JButton btn){
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
	public DefaultTableModel remplirTable(String entree,String strCle) {
		
		String[] column = {"Numero","Titre","Numero Auteur","Annee","Nombre des pages","Cathegorie"};
		DefaultTableModel model = new DefaultTableModel(column,0);
		if(entree.equals("Cathegorie") || strCle.equals("Numero Auteur") || strCle.equals("Numero Livre") ){
			for(Livre livre:listeLivres){
				model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				
			}

		}else{
			int cle = Integer.parseInt(strCle);
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
				for(Livre livre:listeLivres){
					int num = livre.getAuteur();
					if(cle ==num){
						model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				

					}
				}
			}
		}
		return model;

	}

	public  String[] getListeCBox(String choix){
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
			donnee.seek(0);
			for (int i=0;i<donnee.length();i++){
				num = donnee.readInt();
				titre=donnee.readUTF();
				auteur=donnee.readInt();
				annee=donnee.readInt();
				pages=donnee.readInt();
				cathegorie=donnee.readUTF();
				if(num !=-1){
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
			retour[0]="Cathegorie";
		}else if(choix.equals("auteur")) {
			retour[0]="Numero Auteur";
		}else if(choix.equals("num")) {
			retour[0]="Numero Livre";
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
						pathFichier= fc.getSelectedFile().getAbsolutePath();
						nomFichier= fc.getSelectedFile().getName();
					
						if(nomFichier.equals("livres.txt")){ 
					
							tmpReadTxt = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(pathFichier), "UTF8"));
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
								
	}

/*============================================================================================================= */
/*										geters et seters																*/
/*============================================================================================================= */

public HashMap<Integer, Long> getAddresseMap() {
		return addresseMap;
	}
	public void setAddresseMap(HashMap<Integer, Long> addresseMap) {
		this.addresseMap = addresseMap;
	}

	public Long rechercherAddresse(int cle) {
		long adr=-1;
		for(Integer key:addresseMap.keySet()){
			if(key==cle){
				adr=addresseMap.get(key);
				break;
			}
		} 
		return adr;
		
	}
	 public static void main(String[] args) {
		Application frame = new Application();
		frame.setVisible(true);
		
	}


}

