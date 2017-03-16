package Manager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import EngineComm.EngineRequest;
import HTTPRequest.Request;
import Utils.jsonUtils;

public class MainWit {
	
	public static void main(String[] args) {
		
		// TODO : gerer les cas comme " " qui devient "+" avec urlEncoder
		// TODO : mettre urlencoder partout dans l'url
		// TODO : gérer les accents
		// TODO : possibilité de mettre des metadata à chaque value d'entity
		// TODO : gérer les mauvais input de l'utilisateur
		// TODO : faire le liens entre questionss et entités
		
		Request r = new Request();
		EngineRequest er = new EngineRequest();

		String entityId = "firstResponse";

		String doc = "Avez-vous chaud ?";
		
		HashMap<String, ArrayList<String>> values = new HashMap<String, ArrayList<String>>(); 
		
		String value1 = "true";
		ArrayList<String> expressions1 = new ArrayList<String>();
		expressions1.add("Oui");
		expressions1.add("j'ai chaud");
		expressions1.add("Je n'ai pas froid");
		expressions1.add("chauffé");
		values.put(value1, expressions1);
		
		String value2 = "false";
		ArrayList<String> expressions2 = new ArrayList<String>();
		expressions2.add("non");
		expressions2.add("je n'ai pas chaud");
		expressions2.add("j'ai froid");
		values.put(value2, expressions2);
		
		/*
		String value1 = "VOUS_MEME";
		ArrayList<String> expressions1 = new ArrayList<String>();
		expressions1.add("C'est pour moi");
		expressions1.add("moi");
		
		values.put(value1, expressions1);
		
		String value2 = "CONJOINT";
		ArrayList<String> expressions2 = new ArrayList<String>();
		expressions2.add("ma femme");
		expressions2.add("ma compagne");
		expressions2.add("mon épouse");
		expressions2.add("C'est pour ma femme");
		values.put(value2, expressions2);
		
		String value3 = "FILLE";
		ArrayList<String> expressions3 = new ArrayList<String>();
		expressions3.add("ma fille");
		expressions3.add("C'est pour ma fille");
		expressions3.add("C'est pour mon enfant");
		values.put(value3, expressions3);
		
		String value4 = "FILS";
		ArrayList<String> expressions4 = new ArrayList<String>();
		expressions4.add("mon gosse");
		expressions4.add("fils");
		expressions4.add("Mon fils");
		expressions4.add("C'est pour mon enfant");
		values.put(value4, expressions4);*/
		
		/*
		String value1 = "FACTURE_DEVIS";
		ArrayList<String> expressions1 = new ArrayList<String>();
		expressions1.add("Facture-devis");
		expressions1.add("Facture");
		expressions1.add("devis");
		expressions1.add("j'ai une facture");
		expressions1.add("j'ai un devis");
		expressions1.add("Je dois avoir un facture quelque part");
		expressions1.add("Je dois avoir un devis quelque part !");
		
		values.put(value1, expressions1);
		
		String value2 = "ORDONNANCE";
		ArrayList<String> expressions2 = new ArrayList<String>();
		expressions2.add("Ordonnance");
		expressions2.add("j'ai une ordonnance");
		expressions2.add("j'ai une prescription du médecin");
		expressions2.add("le docteur m'a fait une ordonnance");
		values.put(value2, expressions2);
		*/
		

		//r.getEntities();
		
		//r.createEntity(entityId, values, doc);
		
		//r.getSentenceMeaning("response");
		
		//r.setValueToEntity();
		
		//r.setValueToEntityClient("intent", "iceBreak", expressions);
		
		//r.removeValueFromEntity("intent", "+ value +");
		
		//r.UpdateValuesOfEntity(entityId, values, doc);

		//r.getEntityValues(entityId);
		
		//r.getEntities();
		
		//r.getEntityValues(entityId);
		
		//r.UpdateValuesOfEntity(entityId, values, doc);
		
		//r.removeEntity(entityId);
		
		//r.setExpressionToEntity(entityId, value1, "a plus tard");
		
		//r.getEntityValues(entityId);
		
		//r.removeExpressionFromEntity(entityId, value1, "a plus tard");

		//r.getEntityValues(entityId);
		
		
		boolean dialog = true;
		
		if (dialog) {
			String input = er.getFirstInput();
			
			int i = 0;
			boolean keepDialog = true;
			boolean nextQuestion = true;
			
			String output = null;
			
			while (keepDialog) {
				
				if (nextQuestion) {
					output = er.dialogWithEngine(input);
				} else {
					output = er.dialogWithEngine(input);
				}
				

				byte[] b;
				try {
					b = output.getBytes("UTF-8");
					output = new String(b);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				System.out.println("OUTPUT : " + output);
				
				if (er.isAnswer(output)) {  // nextQuestion && 
	
					System.out.println("\nREPONSE FINALE " + i + " : \n");
					System.out.println(er.getAnswer(output));
					keepDialog = false;
				
				} else {
					
	
					System.out.println("\nQUESTION " + i + " : ");
					
					//System.out.println("\nOUPUT : ");
					//System.out.println(output);
					
					System.out.println("\n" + er.getQuestions(output));
					
					Scanner reader = new Scanner(System.in); 
					String response = reader.nextLine();
					System.out.println("Vous avez bien dit : " + response);
					String responseLabel = jsonUtils.getValue(r.getSentenceMeaning(response));
					
					if (responseLabel != null) {
						nextQuestion = true;
					} else {
						nextQuestion = false;
					}
					
					System.out.println("\nresponseLabel : " + responseLabel);
					
					if (nextQuestion)
						input = er.createInput(output, responseLabel);
					else
						input = er.reformulate(input);
					
					System.out.println(input);
					
					//nextQuestion = true;
		
					//System.out.println("\nINPUT : ");
					//System.out.println(input);
				
					i++;
				}
			}
		}
		
		
	}

}
