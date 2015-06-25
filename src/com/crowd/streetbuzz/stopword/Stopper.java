/**
 * 
 */
package com.crowd.streetbuzz.stopword;

import java.util.ArrayList;
import java.util.List;

import kea.stopwords.Stopwords;
import kea.stopwords.StopwordsEnglish;

/**
 * @author Atrijit
 *
 */
public class Stopper {
	 private static final String luceneStandardStopWordList = "a,a's,abaft,able,aboard,about,above,according,accordingly,across,actually,afore,aforesaid,after,afterwards,again,against,agin,ago,ain't,aint,albeit,all,almost,alone,along,alongside,already,also,although,always,am,amid,amidst,among,amongst,an,and,anent,another,any,anybody,anyhow,anyone,anything,anyway,anyways,anywhere,apart,are,aren't,around,as,aside,aslant,at,athwart,available,away,b,back,barring,be,became,because,become,becomes,becoming,been,before,beforehand,behind,being,below,beneath,beside,besides,best,better,between,betwixt,beyond,both,but,by,c,c'mon,c's,came,can,can't,cannot,cant,certain,certainly,circa,clearly,come,comes,comwww,cos,could,couldn't,couldst,currently,d,de,definitely,despite,did,didn't,do,does,doesn't,doing,don't,done,down,downwards,during,durst,e,each,either,else,elsewhere,enough,entirely,ere,especially,et,etc,even,ever,every,everybody,everyone,everything,everywhere,except,excepting,f,failing,far,few,first,five,followed,following,follows,for,from,further,furthermore,g,get,gets,getting,given,gives,go,goes,going,gone,gonna,got,gotta,gotten,h,had,hadn't,hardly,has,hasn't,hast,hath,have,haven't,having,he,he'd,he'll,he's,hence,her,here,here's,hereafter,hereby,herein,hereupon,hers,herself,hi,him,himself,his,hither,how,how's,howbeit,however,i,i'd,i'll,i'm,i've,id,ie,if,il,in,inasmuch,inc,indeed,inner,inside,insofar,instantly,instead,into,inward,is,isn't,it'd,it'll,it's,its,itself,j,just,l,la,lately,latter,latterly,le,least,left,less,lest,let,let's,likely,likewise,m,mainly,many,may,maybe,mayn't,me,meanwhile,merely,mid,midst,might,mightn't,more,moreover,most,mostly,much,must,mustn't,my,myself,n,namely,nd,near,nearly,needn't,needs,neither,never,nevertheless,no-one,nobody,non,none,noone,nor,normally,not,nothing,notwithstanding,now,nowhere,o,o'er,obviously,oc,of,off,oh,ok,okay,on,once,one,ones,oneself,only,onto,or,other,others,otherwise,ought,oughtn't,our,ours,ourselves,out,outside,over,overall,p,particular,particularly,past,per,perhaps,possible,probably,q,qua,que,quite,qv,r,rather,reasonably,regardless,relatively,respectively,s,same,sans,secondly,seem,seemed,seeming,seems,seen,self,selves,seriously,several,shall,shalt,shan't,she,she's,shed,shell,short,should,shouldn't,since,so,some,somebody,somehow,someone,something,sometime,sometimes,somewhat,somewhere,soon,still,such,sure,t,t's,tends,that,that'd,that'll,that's,thats,the,thee,their,their's,theirs,them,themselves,then,thence,there,there's,thereafter,thereby,therefore,therein,theres,thereupon,these,they,they'd,they'll,they're,they've,thine,think,third,this,tho,thorough,thoroughly,those,thou,though,three,thro',through,throughout,thru,thus,thyself,till,to,together,too,toward,towards,truly,u,under,underneath,unfortunately,unless,unlike,unlikely,until,unto,up,upon,usually,v,versus,very,via,vs,w,was,wasn't,way,we,we'd,we'll,we're,we've,were,weren't,wert,what,what'll,what's,whatever,when,when's,whence,whencesoever,whenever,where,where's,whereafter,whereas,whereby,wherein,whereupon,wherever,whether,which,whichever,whichsoever,while,whilst,whither,who'd,who'll,who's,whoever,whole,whom,whore,whose,whoso,whosoever,why,will,willing,wish,with,within,without,won't,wont,would,wouldn't,wouldst,y,ye,yes,yet,yk,you,you'd,you'll,you're,you've,your,yours,yourself,yourselves,january,february,march,april,may,june,july,august,september,october,november,december,s,th,ish,start,starts,period,periods,a,an,and,will,would,can,could,shall,should,are,as,at,although,be,but,by,can,for,from,generally,if,in,into,is,it,no,not,of,on,or,such,that,the,their,then,there,these,they,this,to,was,will,with,not,also,very,often,however,too,usually,really,early,never,always,sometimes,together,likely,simply,generally,instead,actually,again,rather,almost,especial,ever,quickly,probably,already,below,directly,therefore,else,thus,easily,eventually,exactly,certainly,normally,currently,extremely,finally,constantly,properly,soon,specifically,ahead,daily,highly,immediately,relatively,slowly,fairly,primarily,completely,ultimately,widely,recently,seriously,frequently,fully,mostly,naturally,nearly,occasionally,carefully,clearly,essentially,possibly,slightly,somewhat,equally,greatly,necessarily,personally,rarely,regularly,similarly,basically,closely,effectively,initially,literally,mainly,merely,gently,hopefully,originally,roughly,significantl,totally,twice,elsewhere,everywhere,perfectly,physically,suddenly,truly,virtually,altogether,anyway,automaticall,deeply,definitely,deliberately,hardly,readily,terribly,unfortunatel,forth,briefly,moreover,strongly,honestly,previously,as,there,when,how,so,up,out,no,only,well,then,first,where,why,now,around,once,down,off,here,tonight,away,today,far,quite,later,above,yet,maybe,otherwise,near,forward,somewhere,anywhere,please,forever,somehow,absolutely,abroad,yeah,nowhere,tomorrow,yesterday,the,to,in,on,by,more,about,such,through,new,just,any,each,much,before,between,free,right,best,since,both,sure,without,back,better,enough,lot,small,though,less,little,under,next,hard,real,left,least,short,last,within,along,lower,true,bad,across,clear,easy,full,close,late,proper,fast,wide,item,wrong,ago,behind,quick,straight,direct,extra,morning,pretty,overall,alone,bright,flat,whatever,slow,clean,fresh,whenever,cheap,thin,cool,fair,fine,smooth,false,thick,collect,nearby,wild,apart,none,strange,tourist,aside,loud,super,tight,gross,ill,downtown,honest,ok,pray,weekly,accidentally,always,angrily,anxiously,awkwardly,badly,bad,blindly,boastfully,boldlybo,bravelyb,brightly,cheerfully,coyly,crazily,defiantly,deftlyde,deliberately,devotedly,doubtfully,dramatically,dutifully,eagerly,elegantly,enormously,evenly,eventually,exactly,faithfully,finally,foolishly,fortunately,frantically,frequently,gleefully,gracefully,happily,hastily,honestly,hopelessly,hourly,hungrily,innocently,inquisitively,irritably,jealously,justlyju,kindly,the,lazily,loosely,madly,merrily,mortally,mysteriously,nervously,nevernev,obediently,obnoxiously,occasionally,often,oft,only,on,perfectly,politely,poorly,powerfully,promptly,he,quickly,rapidly,rarely,really,regularly,rudely,safely,seldomse,selfishly,seriously,shakily,sharply,silently,slowlysl,solemnly,sometimes,speedily,steadily,sternlys,technically,tediously,tenderly,terrifically,tightly,totally,tremendously,unexpectedly,usuallyu,victoriously,vivaciously,warmly,wearily,weekly,wildly,yearly,used,important,every,large,available,popular,able,basic,known,various,difficult,several,united,historical,hot,useful,mental,scared,additional,emotional,old,political,similar,healthy,financial,medical,traditional,federal,entire,strong,actual,significant,successful,electrical,expensive,pregnant,intelligent,interesting,poor,happy,responsible,cute,helpful,recent,willing,nice,wonderful,impossible,serious,huge,rare,technical,typical,competitive,critical,electronic,immediate,aware,educational,environmenta,global,legal,relevant,accurate,capable,dangerous,dramatic,efficient,powerful,foreign,hungry,practical,psychological,severe,suitable,numerous,sufficient,unusual,consistent,cultural,existing,famous,pure,afraid,obvious,careful,latter,obviously,unhappy,acceptable,aggressive,boring,distinct,eastern,logical,reasonable,strict,successfully,civil,former,massive,southern,unfair,visible,alive,angry,desperate,exciting,friendly,lucky,realistic,sorry,ugly,unlikely,anxious,comprehensive,curious,impressive,informal,inner,pleasant,sexual,sudden,terrible,unable,weak,wooden,asleep,confident,conscious,decent,embarrassed,guilty,lonely,mad,nervous,odd,remarkable,substantial,suspicious,tall,tiny,more,some,one,all,many,most,other,such,even,new,just,good,any,each,much,own,great,another,same,few,free,right,still,best,public,human,both,local,sure,better,general,specific,enough,long,small,less,high,certain,little,common,next,simple,hard,past,big,possible,particular,real,major,personal,current,left,national,least,natural,physical,short,last,single,individual,main,potential,professional,internationa,lower,open,according,alternative,special,working,true,whole,clear,dry,easy,cold,commercial,full,low,primary,worth,necessary,positive,present,close,creative,green,late,fit,glad,proper,complex,content,due,effective,middle,regular,fast,independent,original,wide,beautiful,complete,active,negative,safe,visual,wrong,ago,quick,ready,straight,white,direct,excellent,extra,junior,pretty,unique,classic,final,overall,private,separate,western,alone,familiar,official,perfect,bright,broad,comfortable,flat,rich,warm,young,heavy,valuable,correct,leading,slow,clean,fresh,normal,secret,tough,brown,cheap,deep,objective,secure,thin,chemical,cool,extreme,exact,fair,fine,formal,opposite,remote,total,vast,lost,smooth,dark,double,equal,firm,frequent,internal,sensitive,constant,minor,previous,raw,soft,solid,weird,amazing,annual,busy,dead,false,round,sharp,thick,wise,equivalent,initial,narrow,nearby,proud,spiritual,wild,adult,apart,brief,crazy,prior,rough,sad,sick,strange,external,illegal,loud,mobile,nasty,ordinary,royal,senior,super,tight,upper,yellow,dependent,funny,gross,ill,spare,sweet,upstairs,usual,brave,calm,dirty,downtown,grand,honest,loose,male,quiet,brilliant,dear,drunk,empty,female,inevitable,neat,ok,representati,silly,slight,smart,stupid,temporary,weekly,that,this,what,which,time,these,work,no,only,then,first,money,over,business,his,game,think,after,life,day,home,economy,away,either,fat,key,training,top,level,far,fun,kind,future,action,live,period,subject,mean,stock,chance,beginning,upset,head,material,car,appropriate,inside,outside,standard,medium,choice,north,square,born,capital,shot,front,living,plastic,express,feeling,otherwise,plus,savings,animal,budget,minute,character,maximum,novel,plenty,select,background,forward,glass,joint,master,red,vegetable,ideal,kitchen,mother,party,relative,signal,street,connect,minimum,sea,south,status,daughter,hour,trick,afternoon,gold,mission,agent,corner,east,neither,parking,routine,swimming,winter,airline,designer,dress,emergency,evening,extension,holiday,horror,mountain,patient,proof,west,wine,expert,native,opening,silver,waste,plane,leather,purple,specialist,bitter,incident,motor,pretend,prize,resident";
	private static final List luceneList = new ArrayList();
	 
	 /**
	 * 
	 */
	public Stopper() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean stopKeaDefunct(String input){
		Stopwords stopwords = new StopwordsEnglish();
		boolean bool = stopwords.isStopword(input);
		System.out.println("bool: "+bool);
		return bool;
		
	}
	
	public static boolean stopLucene(String input){
		loadLucene();
		input = input.toLowerCase();
		if(luceneList.contains(input)){
			return true;
		}
		return false;
	}
	
	private static List loadLucene(){
		String [] listarr = luceneStandardStopWordList.split(",");
		
		for (int i=0;i<listarr.length;i++){
			String temp = listarr[i];
			luceneList.add(temp);
		}
		return luceneList;
	}
	
		
	public static StringBuffer stoptaxonomy(String input){
		StringBuffer stoppedBuf = new StringBuffer();
		String[] inputarr = input.split(" ");
		for (int j=0;j<inputarr.length;j++){
			String tempinput = inputarr[j];
			String tempinputsmall = tempinput.toLowerCase();
			boolean isStop = false;
			if(stopLucene(tempinputsmall)){
				isStop = true;
			}
			if(!isStop){
				stoppedBuf.append(tempinput+" ");
			}
			
		}
		return stoppedBuf;
	}
	

	public static List stop(String input){
		String [] listarr = luceneStandardStopWordList.split(",");
		List stopList = new ArrayList();
		List stoppedList = new ArrayList();
		for (int i=0;i<listarr.length;i++){
			String temp = listarr[i];
			stopList.add(temp);
		}
		String[] inputarr = input.split(" ");
		for (int j=0;j<inputarr.length;j++){
			String tempinput = inputarr[j];
			String tempinputsmall = tempinput.toLowerCase();
			if(!stopList.contains(tempinputsmall)){
				stoppedList.add(tempinput);
			}
		}
		return stoppedList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*String example = "Would BJP or AAP win in Delhi";
		List list = stop(example);
		for (int i=0;i<list.size();i++){
			String temp = (String)list.get(i);
			System.out.println(temp);
		}*/
	String temp = "haddock";
	boolean bool = stopLucene(temp);
	System.out.println("is "+bool);
	}

}
