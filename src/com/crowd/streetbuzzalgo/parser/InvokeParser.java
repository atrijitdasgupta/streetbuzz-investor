/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.helper.ParserResourceHelper;
import com.crowd.streetbuzzalgo.parser.vo.BasicDependencyVO;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author Atrijit
 * 
 */
public class InvokeParser implements Constants{

	/**
	 * 
	 */
	public InvokeParser() {
		// TODO Auto-generated constructor stub
		String grammar = BASEEXTLIBPATH+"stanfordnlp/englishPCFG.ser.gz";
		String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		// this.runfromFile(lp, "C:/Mywork/Street Buzz/parserruntests/def.txt");
		String demo = "Best coffee in Pune";

		BasicDependencyVO bdvo = this.runMorefromSentence(lp, demo);
		System.out.println("nsubj:"+bdvo.getNsubj()+",vmod:"+bdvo.getVmod()+",conj:"+bdvo.getConj()+",nn:"+bdvo.getNn()+",prep_in:"+bdvo.getPrep_in()+",conj_and:"+bdvo.getConj_and()+",amod:"+bdvo.getAmod()+",dobj:"+bdvo.getDobj()+",rcmod:"+bdvo.getRcmod()+",root:"+bdvo.getRoot()+",mark:"+bdvo.getMark()+",csubjpass:"+bdvo.getCsubjpass()+",advcl:"+bdvo.getAdvcl());

	}

	public void runfromFile(LexicalizedParser lp, String filename) {
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
			Tree parse = lp.apply(sentence);
			parse.pennPrint();
			System.out.println();

			GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
			Collection tdl = gs.typedDependenciesCCprocessed();
			System.out.println(tdl);
			System.out.println();
		}
	}

	public void runAPI(LexicalizedParser lp) {

	}
	
	private static LexicalizedParser getLexicalParser(){
		ParserResourceHelper prh = ParserResourceHelper.getInstance();
		Map resourcesMap = prh.getResourcesMap();
		LexicalizedParser lp = (LexicalizedParser)resourcesMap.get("lexical");
		return lp;
	}
	public static BasicDependencyVO runMorefromSentence(String input) {
		LexicalizedParser lp =  getLexicalParser();
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok = tokenizerFactory
				.getTokenizer(new StringReader(input));
		List<CoreLabel> rawWords2 = tok.tokenize();
		Tree parse = lp.apply(rawWords2);
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		Collection<TypedDependency> td = gs.typedDependenciesCollapsed();
		//System.out.println(td);
		BasicDependencyVO bdvo = new BasicDependencyVO();
		Object[] list = td.toArray();
		System.out.println(list.length);
		TypedDependency typedDependency;
		List nnList = new ArrayList();
		for (Object object : list) {
			typedDependency = (TypedDependency) object;
			/*System.out.println("Dependency Name: "
					+ typedDependency.dep().nodeString() + " :: "
					+ typedDependency.reln());*/
			String name = typedDependency.reln().getShortName();
			String value = typedDependency.dep().nodeString();
		//	System.out.println(name+" "+value);
			
			if (name.equals("nsubj")) {
				bdvo.setNsubj(value);
			}
			if (name.equals("vmod")) {
				bdvo.setVmod(value);
			}
			if (name.equals("nn")) {
				nnList.add(value);
			}
			if (name.equals("prep_in")) {
				bdvo.setPrep_in(value);
			}
			if (name.equals("amod")) {
				bdvo.setAmod(value);
			}
			if (name.equals("dobj")) {
				bdvo.setDobj(value);
			}
			if (name.equals("rcmod")) {
				bdvo.setRcmod(value);
			}
			if (name.equals("conj")) {
				bdvo.setConj(value);
			}
			if (name.equals("root")) {
				bdvo.setRoot(value);
			}
			if (name.equals("mark")) {
				bdvo.setMark(value);
			}
			if (name.equals("csubjpass")) {
				bdvo.setCsubjpass(value);
			}
			if (name.equals("advcl")) {
				bdvo.setAdvcl(value);
			}
			if (name.equals("conj_and")) {
				bdvo.setConj_and(value);
			}
		}
		bdvo.setNn(nnList);
		return bdvo;
	}

	public static BasicDependencyVO runMorefromSentence(LexicalizedParser lp, String input) {
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok = tokenizerFactory
				.getTokenizer(new StringReader(input));
		List<CoreLabel> rawWords2 = tok.tokenize();
		Tree parse = lp.apply(rawWords2);
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		Collection<TypedDependency> td = gs.typedDependenciesCollapsed();
		//System.out.println(td);
		BasicDependencyVO bdvo = new BasicDependencyVO();
		Object[] list = td.toArray();
		System.out.println(list.length);
		TypedDependency typedDependency;
		List nnList = new ArrayList();
		for (Object object : list) {
			typedDependency = (TypedDependency) object;
			/*System.out.println("Dependency Name: "
					+ typedDependency.dep().nodeString() + " :: "
					+ typedDependency.reln());*/
			String name = typedDependency.reln().getShortName();
			String value = typedDependency.dep().nodeString();
		//	System.out.println(name+" "+value);
			
			if (name.equals("nsubj")) {
				bdvo.setNsubj(value);
			}
			if (name.equals("vmod")) {
				bdvo.setVmod(value);
			}
			if (name.equals("nn")) {
				nnList.add(value);
			}
			if (name.equals("prep_in")) {
				bdvo.setPrep_in(value);
			}
			if (name.equals("amod")) {
				bdvo.setAmod(value);
			}
			if (name.equals("dobj")) {
				bdvo.setDobj(value);
			}
			if (name.equals("rcmod")) {
				bdvo.setRcmod(value);
			}
			if (name.equals("conj")) {
				bdvo.setConj(value);
			}
			if (name.equals("root")) {
				bdvo.setRoot(value);
			}
			if (name.equals("mark")) {
				bdvo.setMark(value);
			}
			if (name.equals("csubjpass")) {
				bdvo.setCsubjpass(value);
			}
			if (name.equals("advcl")) {
				bdvo.setAdvcl(value);
			}
			if (name.equals("conj_and")) {
				bdvo.setConj_and(value);
			}
		}
		bdvo.setNn(nnList);
		return bdvo;
	}

	public void runfromSentence(LexicalizedParser lp, String input) {

		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");
		Tokenizer<CoreLabel> tok = tokenizerFactory
				.getTokenizer(new StringReader(input));
		List<CoreLabel> rawWords2 = tok.tokenize();
		Tree parse = lp.apply(rawWords2);
		// System.out.println(parse.getChild(0).toString());
		/*
		 * System.out.println(parse.getChildrenAsList().get(0));
		 * System.out.println(rawWords2);
		 */
		List leaves = parse.getLeaves();
		for (int i = 0; i < leaves.size(); i++) {
			// System.out.println(leaves.get(i));
		}
		System.out.println(parse.toStringBuilder(new StringBuilder(), true));

		/*
		 * TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
		 * tp.printTree(parse);
		 */
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InvokeParser invokeParser = new InvokeParser();

	}

}
