package bzu.skyn.travehelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TuijianActivity extends Activity {
	private List<Map<String, ?>> data;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuijian);

		data = getData();

		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.tuijian_list, new String[] { "pinpai", "shuoming" },
				new int[] { R.id.pinpai, R.id.shuoming });
		listView = (ListView) findViewById(R.id.tuijian_listview);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Map<String, String> item = (Map<String, String>) data
						.get(position);
				Toast.makeText(TuijianActivity.this,
						item.get("pinpai").toString(), Toast.LENGTH_LONG)
						.show();

			}
		});
	}

	private List<Map<String, ?>> getData() {
		List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("pinpai", "佳园连锁酒店");
		item.put(
				"shuoming",
				"佳园连锁酒店全力打造具有岭南文化内涵的时尚、休闲连锁酒店品牌，成为中小商务客人和观光游人的出行首选。佳园连锁酒店在连锁酒店中独树一帜，提倡超值共分享，一路有佳园的品牌核心价值观，全力为佳园时尚休闲酒店的会员提供时尚休闲、超值、舒适的享受。佳园连锁酒店由香港著名室内设计大师陈俊豪先生设计，大胆地在客房内采用了橙、绿、白三色，热力的橙色用满载激情、温馨点亮房间每个角落；卧床靠枕头一侧的墙面是深遂的绿色，给人以清新雅致、宁静的感觉。橙、绿、白三色组合，让空间变得丰富而多彩。这种用色上的创新，很快就为酒店业所竞相效仿。为了营造时尚感，在卫浴间、大堂等处大量的运用了玻璃，在灯光照射下，以橙色为主色调的玻璃卫浴间折射出温暖与激情；原色的桌椅、沙发、木门，给人以返朴归真的感觉。各类设计元素的运用与组合，既享受了休闲与舒适，又体验了时尚与潮流。 ");
		data.add(item);
		item = new HashMap<String, Object>();
		item.put("pinpai", "汉庭酒店");
		item.put("shuoming", "与7天的单一品牌相比，汉庭连锁酒店集团旗下目前拥有“汉庭快捷”、“汉庭酒店”、“汉庭海友客栈”三个不同市场定位的品牌。汉庭酒店集团对外公布面向全国推出“无停留离店”服务。汉庭会员在入住时付清全部房费，不再支付入住押金，并提前拿到住宿发票。退房时，关上房门即可离店。汉庭的会员卡是一张智能芯片卡，在办理入住手续时，可直接在前台“刷”成临时门卡，离店时直接离开即可。如果没有带会员卡，则在离开时把门卡投入前台“无停留离店”盒子里即可。");
		data.add(item);
		item = new HashMap<String, Object>();
		item.put("pinpai", "锦江之星酒店");
		item.put("shuoming", "实现锦江之星成为出行者对专业、超值、简约、安全、舒适的经济型酒店的首选，公司坚持以专业的水平、务实的精神、真诚的服务，精心塑造锦江之星品牌，不断提升产品的质量，追求简约又时尚、不求奢华但讲究品位的风格，在服务中始终关注客人的体验，将产品的服务内涵与客人的基本需求完美的结合，其特点为健康、安全、舒适的酒店产品，专业、真诚的酒店服务和清新、淡雅的酒店形象。始终保持产品的性价比处于同行最高。酒店特色:保龄球 商务楼层 秘书服务 卡拉OK 酒吧 桑拿浴室 会议厅 豪华套房 会厅 宴会厅 会议室 中餐厅 西餐厅 宽带 会议中心 多功能厅 中央空调 浴室 会客厅 购物 按摩 咖啡厅 送餐服务 桌球 桑拿 棋牌室 冰箱 KTV 客房送餐服务 健身房 沙龙 空调 外币兑换服务 早餐 邮寄 海鲜 乒乓球 叫醒服务 DDD电话 商务 吹风机 商务服务 商务中心 商场 电话 电视 IDD电话 美发 美容 餐厅 银行 IDD.酒店设施:美容 停车场 商场 票务中心 美发 会议室 查看更多 西餐厅 中餐厅 健身房 乒乓球室 台球室 棋牌室 咖啡厅 保龄球 按摩 酒吧 KTV 夜总会 桑拿.房间设施:宽带服务 卫星电视 中央空调 电吹风 房内保险柜 国际直拨电话 国内直拨电话.酒店服务:洗衣服务 商务服务 外币兑换 送餐服务 租车.");
		data.add(item);
		item = new HashMap<String, Object>();
		item.put("pinpai", "七天连锁酒店");
		item.put("shuoming", "7天连锁酒店集团（7 Days Group Holdings Limited）创立于2005年，2009年11月20日在美国纽约证券交易所上市（股票代码：SVN）。作为第一家登陆纽交所的中国酒店集团，7天连锁酒店秉承让顾客“天天睡好觉”的愿景，致力为注重价值的商旅客人提供干净、环保、舒适、安全的住宿服务，满足客户核心的住宿需求。");
		data.add(item);
		
		item = new HashMap<String, Object>();
		item.put("pinpai", "如家酒店");
		item.put("shuoming", "连锁全国商旅便捷无忧:拥有遍及全国100多座城市的600多家连锁酒店，为企业提供全国范围的商旅住宿资源的选择、整合与优化。超值的商旅住宿优惠方案:大客户专享：享受门市价的九折如遇酒店门市价格促销，可在促销价上享受折扣高效便捷地管理公司员工的差旅住宿累积积分，获得更多实惠预订延时保留至19:00在酒店房态紧张时，可享受客房预订优先权可享受免费延时退房的特权：退房时间可延至13:00 我们对如家的企业大客户提供，房价优惠、会员积分、优先预定、预定保留、延时退房等一些列专享的商旅住宿优惠方案。IT技术支持下的信息管理与服务:基于先进的IT技术架构下的服务支持，结合全方位的客户管理系统（CRM），提供全面的商旅住宿信息、价格查询、结算管理与服务质量监控。在优质的住宿服务前提下，让商旅开支情况一目了然，为公司内部管理和控制提供依据。");
		data.add(item);
		item = new HashMap<String, Object>();
		item.put("pinpai", "莫泰168连锁酒店");
		item.put("shuoming", "上海莫泰168-南站店(锦江乐园店),是上海唯一的地铁车站假日酒店，地处沪闵路地铁1号线锦江乐园站虹梅商务大厦9楼，位于沪杭高速公路口。酒店客房设施按三星级标准配备，欧美风格设计。现有122间客房，七种规格的房型任君选择，楼下宽敞的会议室供君使用。");
		data.add(item);
		item = new HashMap<String, Object>();
		item.put("pinpai", "速8酒店");
		item.put("shuoming", "速8酒店（Super 8 Worldwide Inc.）是全球最大的经济型连锁酒店之一（超过2,300家），是全球酒店数量最多（约7,000家）的温德姆酒店集团旗下品牌。速8酒店中国预订中心电话为40018-40018。\n服务理念:1.干净的房间2.友好的服务3.高性价比  速8酒店房间4.免费宽带上网5.24小时热水6.更为便利的地理位置7.24小时网络预订服务8.多样化的酒店装修");
		data.add(item);
		item = new HashMap<String, Object>();
		item.put("pinpai", "斯玛特连锁酒店");
		item.put("shuoming", "斯玛特连锁酒店是加拿大麦克米兰投资公司（MCMILAN INVESTMENT INC）在中国投资的连锁型经济酒店，酒店以鲜明的现代建筑风格成为区内一道亮丽的风景。酒店装修简洁、典雅，大堂温馨、宽敞整洁；酒店拥有布置温馨、居住舒服、服务规范、价格适中、安全卫生的商务套间、家庭间、大床间、标间、单间，免费提供宽带上网、免费停车、交通便利、周边配套设施齐全；斯玛特是您商务、旅行最理想的下榻处。");
		data.add(item);
		item = new HashMap<String, Object>();
		item.put("pinpai", "24K国际连锁酒店");
		item.put("shuoming", "上海24K国际连锁酒店（威海路店）： 24k国际连锁酒店（威海路店）是一家经济型酒店，地处上海市中心区域人民广场附近，毗邻南京路步行街和淮海路商业街，交通便利，经济实惠。 酒店拥有各类客房155间，商务中心、中西式餐厅、咖啡厅、甜品屋等配套设施，服务周到。豪华时尚的设计风格，专业的管家式体贴关怀，服务至上的理念，诠释了现代商务酒店的新概念。\n24K国际连锁酒店（新会路店）9月试营业，位于普陀区新会路139号，与历史名刹“玉佛寺”为邻，地处长寿路商圈，东临新客站不夜城商圈，西连曹家渡商圈，南靠南京西路商圈，北近长寿路中心绿地。地理位置极其优越，交通十分便捷。距地铁1号线、3号线（上海火车站），仅几分钟车程；规划轨道7号线（江宁路站）近在咫尺；10余条公交线路四通八达，立体交通网络汇聚八方人气。\n 24K国际连锁酒店是按三星级标准全新建的经济型酒店，酒店位于汽贸一条街的吴中路上，地处规划中的金虹桥商业街中心，位置优越, 交通便捷。距上海市最大的虹桥商贸城 (在建)仅500米；到达虹桥国际机场、徐家汇商业圈只需车程10分钟；到达虹桥开发区、漕河泾开发区、外环线主干道、内环线高架也只需车程5分钟；毗邻轨道交通九号线合川路站，交通公交线87路、721路、867路、西嘉线、中卫线等可直接到达。旅店内设大型停车场，泊车方便。客房豪华时尚的设计风格，专业的管家式体贴关怀，服务至上的理念，诠释了现代商务酒店的新概念。");
		data.add(item);
		item = new HashMap<String, Object>();
		item.put("pinpai", "安逸158连锁酒店");
		item.put("shuoming", "酒店地址：自贡市自流井区五星街龙都广场 .\n安逸158连锁酒店（自贡店）位于自贡市五星街龙都广场，周边有摩尔玛购物中心、银行、邮局、肯德基等，距离自贡市的彩灯公园仅200米，盐业博物馆仅1公里，门前可乘坐35路公交车直达恐龙博物馆；距离自贡火车站4公里，长途汽车站5公里，乘出租车8分钟即可抵达酒店。自贡店周边金融、邮政、商业、餐饮、娱乐设施一应俱全，商务、出行、购物、休闲十分方便。酒店拥有洁净舒适的各类客房98间，房间宽敞明亮，设施齐备；并提供免费宽带上网，免费长话、免费数字电视，24小时供应热水。酒店商务茶楼可提供小型会议室，餐厅可提供便餐。酒店以“诚信、关怀”的服务理念，让每一位宾客有“宾至如归”之感。安逸158自贡店是您在自贡居停的理想酒店，我们热诚期待您的光临。 酒店与2005年开业，最近装修时间为2010年3，共有客房97间 。");
		data.add(item);
		return data;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tuijian, menu);
		return true;
	}

}
