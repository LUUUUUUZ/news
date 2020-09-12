package com.java.zhutianyao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class Profile{
    String affiliation;
    String affiliation_zh;
    String bio;
    String edu;
    String homepage;
    String note;
    String position;
    String work;
}

public class Scholar {
    static protected LinkedList<Scholar> allScholars;
    String id;
    //头像
    Bitmap avatar;
    //学术活跃度
    double activity;
    //引用数
    int citations;
    //多样性
    double diversity;
    //g指数
    int gindex;
    //h指数
    int hindex;
    //学术合作
    double newStar,risingStar;
    //发表数
    int pubs;
    //社会性
    double sociability;
    //是否是追忆学者
    boolean is_passedaway;
    //名字，name_zh可能为空""
    String name,name_zh;
    Profile prf;
    Map<String,Integer> tags;
    int followed,viewed;
    final static private String ScholarUrl="https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";
    private Scholar(){
        prf=new Profile();
        tags=new HashMap<>();
    }

    @Override
    public String toString(){
        return id+" "+name;
    }

    static protected void init(){
        allScholars=new LinkedList<>();
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    URL u = new URL(ScholarUrl);
                    HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.addRequestProperty("Connection", "Keep-Alive");
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JSONObject jobj=new JSONObject(br.readLine());
                    JSONArray jobj2=jobj.getJSONArray("data");
                    for(int i=0;i<jobj2.length();i++){
                        jobj=jobj2.getJSONObject(i);
                        String tmp=jobj.getString("id");
                        boolean had=false;
                        for(Scholar s:Scholar.allScholars){
                            if(s.id.equals(tmp)){
                                had=true;
                                break;
                            }
                        }
                        if(!had) {
                            Scholar scholar = new Scholar();
                            scholar.id = tmp;

                            String ava = jobj.getString("avatar");
                            URL url1 = new URL(ava);
                            HttpsURLConnection uc = (HttpsURLConnection) url1.openConnection();
                            InputStream inputStream = uc.getInputStream();
                            scholar.avatar = BitmapFactory.decodeStream(inputStream);
                            inputStream.close();

                            scholar.name = jobj.getString("name");
                            scholar.name_zh = jobj.getString("name_zh");
                            scholar.is_passedaway = jobj.getBoolean("is_passedaway");
                            scholar.followed = jobj.getInt("num_followed");
                            scholar.viewed = jobj.getInt("num_viewed");

                            // "indices": {
                            // "activity": 32.7103,
                            // "citations": 11939,
                            // "diversity": 4.2613,
                            // "gindex": 100,
                            // "hindex": 48,
                            // "newStar": 2.1675,
                            // "pubs": 519,
                            // "risingStar": 2.1675,
                            // "sociability": 7.8339}
                            JSONObject ind = jobj.getJSONObject("indices");
                            scholar.activity = ind.getDouble("activity");
                            scholar.citations = ind.getInt("citations");
                            scholar.diversity = ind.getDouble("diversity");
                            scholar.gindex = ind.getInt("gindex");
                            scholar.hindex = ind.getInt("hindex");
                            scholar.newStar = ind.getDouble("newStar");
                            scholar.pubs = ind.getInt("pubs");
                            scholar.risingStar = ind.getDouble("risingStar");
                            scholar.sociability = ind.getDouble("sociability");

                            // "profile": {
                            // "affiliation": "国家呼吸系统疾病临床医学研究中心/国家卫健委/广州呼吸疾病研究所",
                            // "affiliation_zh": "中华医学会",
                            // "bio": "钟南山（1936.10.20- ）呼吸病学学家。福建厦门人。1960年毕业于北京医科大学医疗系，获临床医学学士学位。现任呼吸疾病国家重点实验室主任、国家呼吸疾病临床医学研究中心主任，曾任中华医学会第23任会长。<br><br>通过创制的“简易气道反应性测定法”及流行病学调查，首次证实并完善“隐匿型哮喘”的概念。对我国慢性咳嗽病因谱进行了系统的分析，阐明了胃食道反流性咳嗽的气道神经炎症机制，创制运动膈肌功能测定法。牵头主持我国“十五”科技攻关项目慢性阻塞性肺疾病（COPD）人群防治的系统研究，并获广东省科技进步一等奖（2013年）。在2003年我国SARS疫情中，明确了广东的病原学，组织了广东省SARS防治研究，创建了“合理使用皮质激素，合理使用无创通气，合理治疗并发症”的方法治疗危重SARS患者，获国际上的存活率（96.2%）。组织整理了国内支气管哮喘、慢性阻塞性肺疾病、咳嗽、SARS、人高致病性禽流感等方面的诊治指南文件。2013年任广东省H7N9防控专家组组长，并将H7N9系列研究发表在《New England Journal Medicine》（IF51.658）上，对H7N9防控做出重要贡献。2015年成功治愈广州首例H5N6患者。曾荣获全国先进工作者，全国五一劳动奖章等荣誉称号。<br>1996年当选中国工程院院士。",
                            // "edu": "1960年毕业于北京医学院（现北京大学医学部）并留校任教。1970年到广州医学院进修。\n1979年4月曾到英国进修。\n2007年获得英国爱丁堡大学荣誉博士学位。\n2010年获得澳门科技大学荣誉博士学位。\n1981年8月结束进修归国。",
                            // "homepage": "http://www.cae.cn/cae/html/main/colys/71145511.html ",
                            // "note": "教育来源https://baike.baidu.com/item/%E9%92%9F%E5%8D%97%E5%B1%B1/653914?fr=aladdin<br>无电话、传真、邮箱信息。<br>论文：https://www.ncbi.nlm.nih.gov/pubmed",
                            // "position": "中国工程院院士、主任、组长",
                            // "work": "1960.07-1971.08北京医学院放射医学教研组 助教\n1971.09-1974.04广州医学院第一附属医院内科 住院医师\n1974.04-1978.08 广州医学院第一附属医院内科主治医师\n1978.09-1982.12 广州医学院第一附属医院内科副主任医师、讲师\n1979.10至今 广州呼吸疾病研究所副所长、所长\n1983.01-1986.12 广州医学院副教授\n1986.12至今 广州医学院教授\n1986.12至今 广州医学院呼吸内科硕士导师\n1987.12-1992.10 广州医学院第一附属医院院长\n1992.5-2002.12 广州医学院院长、党委书记\n1995.08至今 北京大学医学部呼吸内科博士生导师\n1996.02-至今中国工程院院士\n2007.10至今 广州呼吸疾病国家重点实验室主任\n2009.7.26至今 任贵州医科大学名誉院长"}
                            JSONObject pro = jobj.getJSONObject("profile");
                            scholar.prf.affiliation = pro.getString("affiliation").replace("<br>","\n");
                            if (pro.has("affiliation_zh")) {
                                scholar.prf.affiliation_zh = pro.getString("affiliation_zh").replace("<br>","\n");
                            } else {
                                scholar.prf.affiliation_zh = "";
                            }
                            if (pro.has("bio")) {
                                scholar.prf.bio = pro.getString("bio").replace("<br>","\n");
                            } else {
                                scholar.prf.bio = "";
                            }
                            if (pro.has("edu")) {
                                scholar.prf.edu = pro.getString("edu").replace("<br>","\n");
                            } else {
                                scholar.prf.edu = "";
                            }
                            if (pro.has("homepage")) {
                                scholar.prf.homepage = pro.getString("homepage").replace("<br>","\n");
                            } else {
                                scholar.prf.homepage = "";
                            }
                            if (pro.has("note")) {
                                scholar.prf.note = pro.getString("note").replace("<br>","\n");
                            } else {
                                scholar.prf.note = "";
                            }
                            if (pro.has("position")) {
                                scholar.prf.position = pro.getString("position").replace("<br>","\n");
                            } else {
                                scholar.prf.position = "";
                            }
                            if (pro.has("work")) {
                                scholar.prf.work = pro.getString("work").replace("<br>","\n");
                            } else {
                                scholar.prf.work = "";
                            }
                            if (jobj.has("tags")) {
                                JSONArray tag = jobj.getJSONArray("tags");
                                JSONArray tags = jobj.getJSONArray("tags_score");
                                for (int j = 0; j < tag.length(); j++) {
                                    String k = tag.getString(j);
                                    int v = tags.getInt(j);
                                    scholar.tags.put(k, v);
                                }
                            }
                            addScholar(scholar);
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    static private void addScholar(Scholar scholar){
        System.out.println(allScholars.size()+" "+scholar.toString());
        if(allScholars.size()==0){
            allScholars.add(scholar);
        }
        else {
            boolean added=false;
            for (int i = 0; i < allScholars.size(); i++) {
                if (allScholars.get(i).activity < scholar.activity) {
                    allScholars.add(i, scholar);
                    added=true;
                    break;
                }
            }
            if(!added){
                allScholars.addLast(scholar);
            }
        }
    }
}
