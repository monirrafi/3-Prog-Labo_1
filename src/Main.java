import java.text.Normalizer;
import java.util.regex.Pattern;
public class Main
{
  public static boolean avecAccent(String chaine){
		//boolean b;
		//b = Pattern.matches("\\p{L}",chaine);
		//return b;
    String strTemp = Normalizer.normalize(chaine, Normalizer.Form.NFD);
    if(chaine.equals(strTemp)){
      return false;
    }else{
      return true;
    }
   // return Pattern.matches("[a-zA-Z]",chaine);
   //return strTemp;
  }
 
    public static String sansAccent(String s) 
  {
 
        String strTemp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(strTemp).replaceAll("");
  }
 
  public static void main(String arg[])
  {
    // Chaîne avec accent
    //String test="Marché public, école, j'ai programmé, chaîne de caractère";
 
    // Effacer les accents de la chaîne de caractère 'test'
   // String str_sans_accent=sansAccent(test);
 
    //Afficher le résultat
    System.out.println(avecAccent("moniré"));

 
  }
 
}
/*
  Résultat:
  Marche public, ecole, j'ai programme, chaine de caractere
 
 */