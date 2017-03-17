package EngineComm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import HTTPRequest.Constants;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;



public class EngineRequest {
	


	
	private String initInput = "/home/pneissen/Documents/fluxXML/templateInput.xml";
	
	private SAXBuilder builder;
	
	private int failureCounter = 0;
	
	/**
	 * Constructor
	 */
	public EngineRequest() {
		builder = new SAXBuilder();
		
		
	}
	
	public String getFirstInput() {

		Document input = null;
		try {
			input = builder.build(new File(initInput));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new XMLOutputter().outputString(input);

	}
	
	public String dialogWithEngine (String contents) {

	    HttpResponse response = null;
	    
	    String result = "";
	    
		try {
			if (contents == null)
				contents = new String(Files.readAllBytes(Paths.get(initInput)));
			//System.out.println(contents);

		    StringEntity xmlEntity = new StringEntity(contents);
			
		    HttpClient client = HttpClientBuilder.create().build();  // setDefaultCredentialsProvider(provider)
		    
		    HttpPost request = new HttpPost(Constants.url);
		    
		    request.setHeader("Data-Type", "xml");
		    request.setHeader("Content-Type", "application/soap+xml; charset=UTF-8");

		    String encoded = Base64.encode((Constants.login2 + ":" + Constants.pwd2).getBytes());
		    //System.out.println("encoded value is " + encoded);
		    
		    request.setHeader("Authorization", "Basic " + encoded);
		    	
		    request.setEntity(xmlEntity);
		    
		    //System.out.println("executing request " + request.getRequestLine());
		    
			response = client.execute(request);
			//System.out.println("Response Code : " + response.getStatusLine());
						
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
			//System.out.println(responseString);

		    /*BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	result += line + "\n";
		    	//System.out.println(line);
		    }
		    rd.close();*/
			
			result = responseString;
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
			
	}

	public String getQuestions(String result) {
		
		String questionToAsk = null;
		
		try {
			//System.out.println(result);
			InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
			Document doc = builder.build(stream);
			
			Element root = doc.getRootElement();
			
			//List children = root.getChildren();
			//System.out.println("List : " + children);
			
			Element ui = root.getChild("ui",  
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#definition"));
			//System.out.println("ui : " + ui);
			
			Element widgets = ui.getChild("widgets",  
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#definition"));
			//System.out.println("widgets : " + widgets);
			
			Element field = widgets.getChild("field",  
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#definition"));
			//System.out.println("field : " + field);
			
			Element question = field.getChild("label");
			//System.out.println("question : " + question);
 
			questionToAsk = new String(question.getText().getBytes("ISO-8859-15"), "UTF-8");
			//System.out.println("Question : " + question.getText());
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return questionToAsk;
	}

	public String createInput(String outputString, String responseLabel) {
		
		InputStream stream;
		
		String result = null;
		
		
		try {
			stream = new ByteArrayInputStream(outputString.getBytes("UTF-8"));  //TODO : nullPointer here
			Document input = builder.build(new File(initInput));
			Document output = builder.build(stream);

			Element inputRoot = input.getRootElement();
			Element outputRoot = output.getRootElement();
			
			Element inputData = inputRoot.getChild("data",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			Element outputDataset = outputRoot.getChild("dataset",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			Element outputData = outputDataset.getChild("data",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			
			/** remove dialog session from template **/
			inputRoot.removeChild("dialog-session",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			
			// Mettre l'élement data dans l'input.xml
			inputRoot.removeChild("data",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			inputRoot.addContent((Element)outputData.clone());
			
			Element outputDataref = outputRoot.getChild("dataref",
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#binding"));
			//List children = outputDataref.getChildren();
			//System.out.println("List : " + children);
			Element inputDatarefValue = outputDataref.getChild("value",
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#binding"));
			
			String path = inputDatarefValue.getAttributeValue("path");
			//path = path.substring(0,path.length()-5);
			path = "//" + path;
			//path = path.substring(0, path.length()-5);
			//System.out.println(path);
			
			String[] parts = path.split("/");
			//System.out.println("last parts : " + parts[parts.length-1].charAt(0));
			
			if (parts[parts.length-1].charAt(0) == '@') {
				XPathExpression<Attribute> xpAttr = XPathFactory.instance().compile(path, Filters.attribute(), null, 
															Namespace.getNamespace("y", "http://www.yseop.com/engine/3"));
				//xp.setVariable("yid", Namespace.getNamespace("y", "http://www.yseop.com/engine/3"), responseLabel);
				Attribute e = (Attribute)(xpAttr.evaluate(input).get(0));
				//e.setAttribute("yid", responseLabel);
				e.setValue(responseLabel);
			} else {
				XPathExpression<Element> xpElem = XPathFactory.instance().compile(path, Filters.element(), null, 
						Namespace.getNamespace("y", "http://www.yseop.com/engine/3"));
				
				Element e = xpElem.evaluate(input).get(0);
				e.setText(responseLabel);
			}
				
			
			
			
			//TODO : en fait il faut mettre tous les fils de dataset à la racine de l'input
			
			/** gathering store **/
			Element outputGatheringStore = outputDataset.getChild("gathering-store",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			inputRoot.addContent(outputGatheringStore.clone());
			
			/** uiInstance **/
			Element outputUiinstance = outputRoot.getChild("uiinstance",
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#instance"));
			inputRoot.addContent((Element)outputUiinstance.clone());
			
			
			/**  <y:action command="next-request" sub-command="operNext"/> **/
			Element outputUi = outputRoot.getChild("ui",
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#definition"));
			Element outputWidgets = outputUi.getChild("widgets",
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#definition"));
			Element outputSubmit = outputWidgets.getChild("submit",
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#definition"));
			Attribute outputCommand = outputSubmit.getAttribute("command");
			Attribute outputSubCommand = outputSubmit.getAttribute("sub-command");
			Element inputAction = inputRoot.getChild("action",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			Attribute inputCommand = inputAction.getAttribute("command");
			inputCommand.setValue(outputCommand.getValue());
			inputAction.setAttribute(outputSubCommand.clone());
			
			/** dialog session **/
			Element outputDialogSession = outputDataset.getChild("dialog-session",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			if (failureCounter > 0) {
				outputDialogSession.removeChild("dialog-response-failure-count",
						Namespace.getNamespace("http://www.yseop.com/engine/3"));
				failureCounter = 0;
			}
			inputRoot.addContent(outputDialogSession.clone());
			
			
			result = new XMLOutputter().outputString(input);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;

	}

	
	public boolean isAnswer(String outputString) {
		
		InputStream stream;
		
		Boolean result = false;
		
		try {
			stream = new ByteArrayInputStream(outputString.getBytes("UTF-8"));
			Document output = builder.build(stream);

			Element outputRoot = output.getRootElement();
			
			Element outputDataset = outputRoot.getChild("dataset",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			Element outputUiinstance = outputRoot.getChild("uiinstance",
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#instance"));
			
			String attrToCheck = outputUiinstance.getAttribute("id").getValue();
			
			if (attrToCheck.equals("template-Answer")) 
				result = true;
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public String getAnswer(String outputString) {
		
		InputStream stream;
		
		String result = null;
		
		try {
			stream = new ByteArrayInputStream(outputString.getBytes("UTF-8"));
			Document output = builder.build(stream);

			Element outputRoot = output.getRootElement();
			
			Element outputUiinstance = outputRoot.getChild("uiinstance",
					Namespace.getNamespace("http://apache.org/cocoon/forms/1.0#instance"));
			
			List<Element> outputZoneList = outputUiinstance.getChildren();
			
			for (Element e : outputZoneList) {
				if (e.getAttribute("id").getValue().equals("finalTextZone")) {
					result = e.getChildText("txt",
							Namespace.getNamespace("http://www.yseop.com/engine/3"));
				}
			}
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public String reformulate(String inputString) {
		
		InputStream stream;
		
		String result = null;
		
		
		try {
			stream = new ByteArrayInputStream(inputString.getBytes("UTF-8"));  //TODO : nullPointer here
			Document input = builder.build(stream);

			Element inputRoot = input.getRootElement();

			Element inputDialogSession = inputRoot.getChild("dialog-session",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			//System.out.println("inputDialogSession : " + inputDialogSession);
			
			if (failureCounter == 0) {
				String s = "<y:dialog-response-failure-count xmlns:y=\"http://www.yseop.com/engine/3\"></y:dialog-response-failure-count>";
				InputStream st = new ByteArrayInputStream(s.getBytes("UTF-8")); 
				Document failDoc = builder.build(st);
				Element failRoot = failDoc.getRootElement();
				//System.out.println("failRoot : " + failRoot);
				inputDialogSession.addContent(failRoot.clone());
			}
		
			Element inputDialogResponseFailureCount = inputDialogSession.getChild("dialog-response-failure-count",
					Namespace.getNamespace("http://www.yseop.com/engine/3"));
			failureCounter++;
			inputDialogResponseFailureCount.setText(Integer.toString(failureCounter));
				
			result = new XMLOutputter().outputString(input);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;

	}
	
	
	
}
