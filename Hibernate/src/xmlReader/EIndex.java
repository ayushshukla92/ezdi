package xmlReader;

import javax.persistence.*;

@Entity
@Table(name = "EIndex")
public class EIndex {
   @Id @GeneratedValue
   @Column(name = "id")
   private int id;

   @Column(name = "nec")
   private Boolean NEC;
   
   @Column(name = "code")
   private String code;


   @Column(name = "nos")
   private Boolean NOS;
   
   @Column(name = "description")
   private String description;

   @Column(name = "description_with_nemod")
   private String descriptionWithNemod;
   
   @Column(name = "hierarchy")
   private short[] hirarchy;  
   
   @Column(name = "level")
   private byte level;  
   
   @Column(name = "see")
   private String see;
   
   @Column(name = "see_also")
   private String seeAlso;
   
   @Column(name = "see_category")
   private String seeCategory;
   
   @Column(name = "sub_category")
   private String subCategory;
   
   @Column(name = "title")
   private String title;


   public EIndex() {}
   
   public int getId() {
      return id;
   }
   
   public void setSee(String see) {
       this.see = see;
   }
   public String getSee() {
       return see;
   }
  
    public String getSeeCategory() {
        return seeCategory;
    }

    public void setSeeCategory(String seeCategory) {
        this.seeCategory = seeCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(String seeAlso) {
        this.seeAlso = seeAlso;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionWithNemod() {
        return descriptionWithNemod;
    }

    public void setDescriptionWithNemod(String descriptionWithNemod) {
        this.descriptionWithNemod = descriptionWithNemod;
    }

    public Boolean getNEC() {
        return NEC;
    }

    public void setNEC(Boolean nEC) {
        NEC = nEC;
    }

    public Boolean getNOS() {
        return NOS;
    }

    public void setNOS(Boolean nOS) {
        NOS = nOS;
    }

    public short[] getHirarchy() {
        return hirarchy;
    }

    public void setHierarchy(short[] hirarchy) {
        this.hirarchy = hirarchy;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

   
}