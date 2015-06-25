/**
 * 
 */
package com.crowd.streetbuzzalgo.interestdetection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crowd.streetbuzz.processhelperutils.WebSiteSearch;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.taxonomy.conceptnet.ConceptNet;
import com.crowd.streetbuzzalgo.utils.CollectionsUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class InterestDetector {
	private static final String intereststring = "business,technology,culture,entertainment,education,work,family,living,fashion,beauty,food,drink,gadget,applicance,health,fitness,home,garden,motor,vehicle,nature,climate,society,politics,economy,religion,philosophy,sport,hobby,travel,tourism,science,medicine";

	private static final String secondaryintereststring = "concern,business concern,business organization,business organisation,commercial enterprise,business enterprise,occupation,job,line of work,line,business sector,clientele,patronage,stage business,byplay,acting,activity,aim,business activity,commerce,commercial activity,commercialism,enterprise,headache,mercantilism,object,objective,people,performing,playacting,playing,sector,target,vexation,worry,orgnization,organisation,#,engineering,engineering science,applied science,application,bailiwick,branch of knowledge,discipline,field,field of study,practical application,profession,study,subject,subject area,subject field,#,civilization,civilisation,acculturation,polish,refinement,cultivation,finish,appreciation,attitude,cognitive content,content,development,discernment,flawlessness,growing,growth,maturation,mental attitude,mental object,ne plus ultra,ontogenesis,ontogeny,perceptiveness,perfection,society,taste,#,amusement,diversion,recreation,#,instruction,teaching,pedagogy,didactics,educational activity,training,breeding,Department of Education,Education Department,Education,acquisition,activity,cognitive content,content,executive department,learning,mental object,profession,upbringing,#,piece of work,employment,study,workplace,oeuvre,body of work,acquisition,activity,business,end product,energy,geographic point,geographical point,job,learning,line,line of work,occupation,output,product,production,work,do work,act,function,operate,go,run,work on,process,exercise,work out,make,bring,play,wreak,make for,put to work,cultivate,crop,influence,act upon,shape,form,mold,mould,forge,knead,exploit,solve,figure out,puzzle out,lick,ferment,sour,turn,affect,apply,be,bear on,bear upon,becharm,become,beguile,bewitch,bring home the bacon,captivate,capture,care,catch,change state,charm,come through,convert,create,create from raw material,create from raw stuff,deal,deliver the goods,displace,employ,enamor,enamour,enchant,entrance,excite,fascinate,fix,gear up,get,go across,go through,handle,impact,manage,manipulate,move,pass,prepare,proceed,ready,set,set up,stimulate,stir,succeed,touch,touch on,trance,transform,transmute,transubstantiate,understand,use,utilise,utilize,win,idle,malfunction,bring off,bring on,bring up,#,household,house,home,menage,family unit,class,category,family line,folk,kinfolk,kinsfolk,sept,phratry,kin,kinsperson,syndicate,crime syndicate,mob,fellowship,accumulation,aggregation,ancestry,assemblage,association,blood,blood line,bloodline,clan,collection,descent,gangdom,gangland,kin group,kindred,kinship group,line,line of descent,lineage,organized crime,origin,parentage,pedigree,relation,relative,social unit,stemma,stock,taxon,taxonomic category,taxonomic group,tribe,unit,#,surviving,people,absolute,extant,live,realistic,life,animation,aliveness,support,keep,livelihood,bread and butter,sustenance,being,beingness,existence,experience,people,resource,dead,#,manner,mode,style,way,consumer goods,pattern,practice,property,trend,vogue,forge,make,#,smasher,stunner,knockout,ravisher,sweetheart,peach,lulu,looker,mantrap,dish,beaut,adult female,appearance,example,exemplar,good example,model,visual aspect,woman,ugliness,#,nutrient,solid food,food for thought,intellectual nourishment,cognitive content,content,matter,mental object,solid,substance,#,drinking,boozing,drunkenness,crapulence,beverage,drinkable,potable,swallow,deglutition,body of water,consumption,food,helping,ingestion,intake,intemperance,intemperateness,liquid,nutrient,portion,serving,uptake,water,imbibe,booze,fuddle,toast,pledge,salute,wassail,drink in,tope,absorb,consume,engross,engulf,habituate,have,honor,honour,immerse,ingest,plunge,reward,soak up,steep,take,take in,use,drink down,drink up,#,contraption,contrivance,convenience,gizmo,gismo,widget,device,#,consumer durables,device,durable goods,durables,#,wellness,condition,eudaemonia,eudaimonia,status,upbeat,welfare,well-being,wellbeing,illness,unwellness,#,fittingness,physical fitness,seaworthiness,competence,competency,condition,good condition,good shape,shape,soundness,suitability,suitableness,unfitness,internal,national,interior,away,location,domestic,place,dwelling,domicile,abode,habitation,dwelling house,home plate,home base,plate,base,family,household,house,menage,nursing home,rest home,bag,beginning,environment,housing,institution,living accommodations,location,lodging,origin,residence,root,rootage,social unit,source,unit,come back,domiciliate,get back,go back,house,put up,return,#,botany,curtilage,flora,grounds,patch,plot,plot of ground,vegetation,yard,tend,#,centrifugal,motive,causative,efferent,motorial,agent,machine,drive,go,locomote,move,travel,#,fomite,conveyance,matter,medium,object,physical object,substance,transport,#,causal agency,causal agent,cause,cosmos,creation,existence,macrocosm,quality,trait,type,universe,world,#,clime,mood,condition,environmental condition,status,#,club,social club,guild,gild,lodge,order,company,companionship,fellowship,high society,beau monde,smart set,bon ton,association,elite,elite group,friendly relationship,friendship,social group,#,political relation,political science,government,political sympathies,opinion,persuasion,profession,sentiment,social relation,social science,thought,view,policy,insurance policy,insurance,argumentation,contract,line,line of reasoning,logical argument,plan of action,#,economic system,thriftiness,saving,action,efficiency,frugality,frugalness,scheme,system,#,faith,religious belief,organized religion,belief,establishment,institution,supernatural virtue,theological virtue,#,doctrine,philosophical system,school of thought,ism,arts,belief,humanistic discipline,humanities,liberal arts,#,athletics,summercater,sportsman,sportswoman,mutant,mutation,variation,fun,play,athlete,being,business,diversion,humor,humour,individual,job,jock,line,line of work,mortal,occupation,organism,person,recreation,somebody,someone,soul,vacationer,vacationist,wit,witticism,wittiness,feature,boast,frolic,lark,rollick,skylark,disport,cavort,gambol,frisk,romp,run around,lark about,have,play,#,avocation,by-line,pursuit,sideline,spare-time activity,hobbyhorse,rocking horse,Falco subbuteo,falcon,interest,pastime,plaything,toy,#,traveling,travelling,change of location,locomotion,motion,move,movement,travel,go,move,locomote,journey,trip,jaunt,move around,stay in place,go around,go by,go down,go off,go on,go out,go under,go up,move back,move on,move out,#,touristry,business,business enterprise,commercial enterprise,#,scientific discipline,skill,ability,bailiwick,branch of knowledge,discipline,field,field of study,power,study,subject,subject area,subject field,#,medical specialty,medication,medicament,medicinal drug,practice of medicine,music,drug,learned profession,medical science,penalisation,penalization,penalty,punishment,medicate,care for,treat";

	private static final List interestlist = new ArrayList();

	private static final List secondaryinterestlist = new ArrayList();

	/**
	 * 
	 */
	public InterestDetector() {
		// TODO Auto-generated constructor stub
	}

	public static List detectInterest(String topic) throws Exception {
		List list = new ArrayList();
		list =  runInterestDetection(topic);
		return list;
	}
	
	private static List altDetect(String topic){
		List list = new ArrayList();
		List topiclist = new ArrayList();
		topiclist.add(topic);
		List googleResultList = WebSiteSearch.googleSearch(topiclist);
		List truncatedlist;
		if (googleResultList.size() > 6) {
			truncatedlist = googleResultList.subList(0, 4);
		} else {
			truncatedlist = googleResultList;
		}
		List urllist = new ArrayList();
		for (int i = 0; i < truncatedlist.size(); i++) {
			Result result = (Result) truncatedlist.get(i);
			String url = StrUtil.nonNull(result.getUrl());
			urllist.add(url);
			
		}
		return list;
	}

	private static void load() {
		String[] temp = intereststring.split(",");
		for (int i = 0; i < temp.length; i++) {
			String tmp = temp[i];
			tmp = tmp.trim();
			interestlist.add(tmp);
		}

		String[] sectmp = secondaryintereststring.split("#");
		for (int i = 0; i < sectmp.length; i++) {
			String tmp = sectmp[i];
			// System.out.println(tmp);
			String[] tmparr = tmp.split(",");
			List list = new ArrayList();
			for (int j = 0; j < tmparr.length; j++) {
				String nextmp = tmparr[j];
				nextmp = nextmp.trim();
				if (!"".equalsIgnoreCase(nextmp)) {
					if (!list.contains(nextmp)) {
						list.add(nextmp);
					}

				}
			}
			secondaryinterestlist.add(list);
		}
	
	}

	private static List runInterestDetection(String topic) throws Exception {
		load();
		
		
		Map mapofinterest = new HashMap();
		
		List termslist = new ArrayList();
		
		termslist.add(topic);
		List finalconceptlist = ConceptNet.conceptNet(termslist);

		for (int i = 0; i < finalconceptlist.size(); i++) {
			String concept = (String) finalconceptlist.get(i);
			System.out.println("concept is "+concept);
			for (int j = 0; j < interestlist.size(); j++) {
				String interest = (String) interestlist.get(j);
				Long temp = new Long(j + 1);
				String tempstr = temp.toString();
				if (concept.indexOf(interest) > -1) {
					System.out.println("1 interest is "+interest);
					if (mapofinterest.containsKey(tempstr)) {
						Long val = (Long) mapofinterest.get(tempstr);
						int vint = val.intValue() + 1;
						mapofinterest.put(tempstr, new Long(vint));

					} else {
						mapofinterest.put(tempstr, new Long(1));
					}

				}
			}

			for (int j = 0; j < secondaryinterestlist.size(); j++) {
				List list = (List) secondaryinterestlist.get(j);
				for (int k = 0; k < list.size(); k++) {
					String interest = (String) list.get(k);
					if (concept.indexOf(interest) > -1) {
						System.out.println("2 interest is "+interest);
						Long temp = new Long(j + 1);
						String tempstr = temp.toString();
						if (mapofinterest.containsKey(tempstr)) {
							Long val = (Long) mapofinterest.get(tempstr);
							int vint = val.intValue() + 1;
							mapofinterest.put(tempstr, new Long(vint));

						} else {
							mapofinterest.put(tempstr, new Long(1));
						}
					}
				}
			}
		}
		System.out.println("here");
		Map sortedmap = CollectionsUtils.sortByLongValue(mapofinterest);
		ArrayList<String> poskeys = new ArrayList<String>(sortedmap.keySet());
		List descinterestlist = new ArrayList();
		for (int i = poskeys.size() - 1; i >= 0; i--) {
			String posword = (String) poskeys.get(i);
		//	System.out.println("posword: " + posword);
			Long posvalue = (Long) sortedmap.get(posword);
			descinterestlist.add(new Long(posword));
			if (descinterestlist.size() == 4) {
				break;
			}
		}
		List finalList = getInterestId(descinterestlist);
		System.out.println(finalList);
		return finalList;
	}

	private static List getInterestId(List list) {
		List iList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Long temp = (Long) list.get(i);
			int tint = temp.intValue();
			if (tint == 1) {
				iList.add(new Long(1));
			}
			if (tint == 2) {
				iList.add(new Long(1));
			}
			if (tint == 3) {
				iList.add(new Long(2));
			}
			if (tint == 4) {
				iList.add(new Long(2));
			}
			if (tint == 5) {
				iList.add(new Long(3));
			}
			if (tint == 6) {
				iList.add(new Long(3));
			}
			if (tint == 7) {
				iList.add(new Long(4));
			}
			if (tint == 8) {
				iList.add(new Long(4));
			}
			if (tint == 9) {
				iList.add(new Long(5));
			}
			if (tint == 10) {
				iList.add(new Long(5));
			}
			if (tint == 11) {
				iList.add(new Long(6));
			}
			if (tint == 12) {
				iList.add(new Long(6));
			}
			if (tint == 13) {
				iList.add(new Long(7));
			}
			if (tint == 14) {
				iList.add(new Long(7));
			}
			if (tint == 15) {
				iList.add(new Long(8));
			}
			if (tint == 16) {
				iList.add(new Long(8));
			}
			if (tint == 17) {
				iList.add(new Long(9));
			}
			if (tint == 18) {
				iList.add(new Long(9));
			}
			if (tint == 19) {
				iList.add(new Long(10));
			}
			if (tint == 20) {
				iList.add(new Long(10));
			}
			if (tint == 21) {
				iList.add(new Long(11));
			}
			if (tint == 22) {
				iList.add(new Long(11));
			}
			if (tint == 23) {
				iList.add(new Long(11));
			}
			if (tint == 24) {
				iList.add(new Long(12));
			}
			if (tint == 25) {
				iList.add(new Long(12));
			}
			if (tint == 26) {
				iList.add(new Long(13));
			}
			if (tint == 27) {
				iList.add(new Long(13));
			}
			if (tint == 28) {
				iList.add(new Long(14));
			}
			if (tint == 29) {
				iList.add(new Long(14));
			}
			if (tint == 30) {
				iList.add(new Long(15));
			}
			if (tint == 31) {
				iList.add(new Long(15));
			}
			if (tint == 32) {
				iList.add(new Long(16));
			}
			if (tint == 33) {
				iList.add(new Long(16));
			}
		}
		return iList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			long start = System.currentTimeMillis();
			detectInterest("Mumbai pav bhaji");
			long end = System.currentTimeMillis();
			System.out.println("time: " + (end - start));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
