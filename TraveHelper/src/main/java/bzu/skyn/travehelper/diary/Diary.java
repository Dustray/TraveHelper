package bzu.skyn.travehelper.diary;

public class Diary {
	private Integer id;
	private String title;
	private String content;
	private String createtime;
	
	public Diary() {
		super();
	}

	public Diary(String title, String content, String createtime) {
		super();
		this.title=title;
		this.content=content;
		this.createtime=createtime;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "日记标题" + getTitle() + "\n日记内容：" + getContent() + "\n创建时间为："
		+ getCreatetime();
	}
}
