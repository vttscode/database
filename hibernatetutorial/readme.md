### Hibernate

 * Configuration
 * Entity
 * Entity Mapping
 * Persistence
 * HQL
 * Criteria
 * Caching
 
 ### Configuration
 Gali būti daug konfiguracijos būdų:
 
 1. JPA konfiguracija. Ši konfiguracija skirta Java Persitance API, tačiau pakeitus `<provider>` tag'o reikšmę galima nustatinėti Hibernate konfiguraciją.  
 Tai padaryti classpath reikia turėti direktoriją/failą: META-INF/persistance.xml
    
```xml
    <?xml version="1.0" encoding="UTF-8" ?>
    <persistence xmlns="http://java.sun.com/xml/ns/persistence"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
     http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd" version="1.0">
    
       <persistence-unit name="my-pu">
         <description>My Persistence Unit</description>
         <provider>org.hibernate.ejb.HibernatePersistence</provider>
         <properties>
            <property name="hibernate.dialect" 
                                     value="org.hibernate.dialect.MySQLDialect"/>
            <property name="javax.persistence.jdbc.url"
                     value="jdbc:mysql://localhost:3306/playground"/>
            <property name="javax.persistence.jdbc.user" value="admin"/>
            <property name="javax.persistence.jdbc.password" value="admin"/>
         </properties>
       </persistence-unit>
    
    </persistence>
```
    
Tuomet jungiantis prie duombazės sukurti `Configuration` objektą iš kurio galima gauti `Session` objektą skirtą manipuliuoti `Entity` objektais duombazėje:
 ```java
Configuration config = new Configuration().configure(); //pridėti klases visu sukurtu Entities;
Session session = config.buildSessionFactory().getCurrentSession();
```
Komentare `//pridėti klases visu sukurtu Entities;` turima omeny pridėti visass sukurtas entities  klases per 'addAnnotatedClass(Class class)'
Pvz:
```java
Configuration config = new Configuration()
    .addAnnotatedClass(Person.class)
    .configure();
```
Atreipkite dėmesį, kad `configure()` pridedama po anotuotų klasių pridėjimo.

2. Kitas variantas yra turėti `hibernate.cfg.xml` failą savo classpath'e.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/playground?serverTimezone=UTC</property>
        <property name="hibernate.connection.username">medardas</property>
        <property name="hibernate.connection.password">pass</property>
        <property name="hibernate.connection.pool_size">1</property>
        <property name="hibernate.current_session_context_class">thread</property>
        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>

    </session-factory>
</hibernate-configuration>
```
Čia yra standartinis Hibernate configuracijos variantas.
Sukurti `Session` galima lygiai tokiu pačiu būdu kaip priš tai.

### Entity

`Enitity` yra Java objektas kuris turi tiesioginį sąryšį savo field'ais su lentelės stulpeliais duombazėje.
Taigi jei tarkim egzistuoja lentelė:
```
\-------------------/
|       Person      |
---------------------
| ID |   Name | age |
--------------------
|  1 |  Jonas |  10 |
|  2 |   Ieva |  18 |
/-------------------\
```
Jos `Entity` bū java objektas su anotacija `@Entity`
```java
@Entity
@Data
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;
}
``` 
Čia Hibernate framework'as išsiaiškina kokios yra stulpelių reikšmės pagal tai kokie `Entity` objekte yra field'ų pavadinimai: `id`, `name`, `age`. Taip pat išsiaiškina stalo pavadinimą. 
   * `@Id` nusako, kad tai yra `PRIMARY KEY` reikšmė.
   * `@GeneratedValue(strategy = GenerationType.IDENTITY)` nusako, kad ši reikšmė yra auto-generuojama.
   
### Entity mapping
Viena sudėtingiausių dalių dirbant su hibernate yra teisingai sumappinti java objektus su lentlėmis.
Tačiau teisingai sukūrus šį sąryšį galima labai pasilengivnti darbą su savo sistema.
Taigi, jei egzituoja antra lentelė kur `PersonId` yra `Foreign Key` į `Person(id)`.
```
\------------------/
|        Salary    |
-------------------- 
| PersonId |  Pay |
--------------------
|         2 | 1000 | 
|         1 | 1200 | 
/------------------\
```

Kad ją galėtume surišti su `Person` objektu mūsų naujas `Entity` gali atrodyti taip:
```java

@Entity
@Data
@NoArgsConstructor
public class Salary {
    @Id
    private int personId;
    private int pay;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "personId", referencedColumnName = "id")
    private Person person;
}

```
O `Person` turėtų gauti nauja field'ą:
```java
@OneToOne(mappedBy = "person")
private Salary salary;
```

Šis detales hibernate supranta taip:
 * Hiberate žino, kad Person Primary key yra `Id`
 * Jis taip pat žino, kad primary key pas Salary yra `personId`
 * Sukurtas naujas field'as `salary` Objekte `Person` nurodo, kad `Person` turi `Salary` ir jų ryšys yra abiejų objektų Primary Key, tai yra `Person.id = Salary.personId`
 * `cascade = CascadeType.ALL` reiškia, kad jei rekšmė pasikeis šiame objekte, ji taip pat pasikeis ir `Person` objekte ir atitinkamais pakeitimais duombazėje bus pasirūpinta.

 
### Persistence

```java
Session session = hibernateCfg.buildSessionFactory().openSession();
session.getTransaction().begin();

Person person = new Person();
person.setName("Petras");
person.setAge(30);
session.save(person);
Salary salary = new Salary();
salary.setPay(1000);
salary.setPersonId(person.getId());
session.save(salary);
        
session.getTransaction().commit();
       
session.close();
```
Šiame kodo gabaliuke pirmiausia yra sukuriama sesijos `Session` objektas iš konfiguracijos kuri būvo nusakyta ankstesniame skyriuje.
Tuomet norint pradėti darbą su `Enity` objektais ir jų įrašymu į duombazę reikia atidaryti transakcija. Transakcija yr atidaroma su
`session.getTransaction().begin();` ir baigiama su `session.getTransaction().commit();`
Taigi norint daryti update, insert ir delete komandas - jas būtina daryti dar atidarytos transakcijos.
`Select` tipo statement'am tai nebūtina, galima tiesiog naudoti funkcijas tokias kaip `session.find(class, id)`

Atsijungimas nuo duombazės visiškai yra tvarkomas kaip sesijos uždarymas: `session.close();`;

### HQL - Hibernate Query Language

Tai yra į objektus orentuota užklaušų kalba (object-oriented query language) kuri yra panaši į SQL tačiau vietoj lentelių
ir stulpelių ji dirba su objektais ir jų field'ais. Hibernate išverčia šiuos objektus į standartinę SQL užklausą ir siunčia tai į duombazę.
IntelliJ netgi gali debuginti šitas užklausas java failuose.

Nors per Hibernate galima leisti ir Native SQL užklausas, naudodami HQL mes galime naudotis hibernate caching galimybėmis ir šiek tiek mažiau nerimauti apie
teisingas užklausas. 

Lentelių ir field'u pavadinimai yra case sensitive, tačiau SQL raktažodžiai nėra.
```java
String hql = "FROM Employee";
Query query = session.createQuery(hql);
List<Employee> results = query.list();
```
Tokia programa pirmoje eilutėje nusako su kokiu objektu reikia dirbti (`Employee`), tuomet antroje - sukurti SQL užklausą iš hibernate sesijos
ir paskutinis dalykas yra iškviesti `list()` metoda kuris gražina paprastą SQL (`SELECT * FROM Employee`) rezultatą kaip sąrašą.

Lygiai toks pats rezultatas būtu su:

```java
String hql = "FROM com.codeacademy.hibernatetutorial.model.Employee";
Query query = session.createQuery(hql);
List<com.codeacademy.hibernatetutorial.model.Employee> results = query.list();
```
Tokiu būdu galima sukurti praktiškai betkokią HQL užklausą visom CRUD operacijoms. Reikia atsiminti, kad tai yra tas pats kas SQL tik dirbant su objektais ir jų field'ais reikia galvoti apie juos kaip apie stalus ir
stulpelius tiesiogiai.

##### Named parameters

HQL užklausose galima nustatinėti parametrus:
```java
String hql = "FROM Employee E WHERE E.id = :employee_id";
Query query = session.createQuery(hql);
query.setParameter("employee_id",10);
List results = query.list();
```

Tokiu būdu sugalvojame parametro pavadinimą ir idedame jį su dvidatškiu, kad hibernate suprastu tai kaip parametrą (`:employee_id`)
Tuomet nustatyti parametro reikšmę galima programiniu būdu `query.setParameter("employee_id",10);`

### Criteria

Criteria yra  objektas kuris gali per programines funkcijas sukurti kompleksią užklausą kuri po to išverčiama į SQL kalbą.
Hibernate į inicijuoti kriterijų galima taip:
```java
CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);
criteriaQuery.where(criteriaBuilder.equal(employeeRoot.get("name"), "Jooohn"));
List<Employee> employees = session.createQuery(criteriaQuery).getResultList();
```
Čia pirmiausia sukuriamas `CriteriaBuilder` objektas kuris gali sukurti užklausas ir jose lyginti loginius paramtrus.
Užklausos yra kuriamos (`CriteriaQuery`) remiantis kažkokia lentele, kadangi mes norim sužinoti apie `Employee`, mes nurodome atitinkamos `Enity` klase 
ir sukuriam `SELECT` užklausą (krierijai gali būti tik užklausose `SELECT`, `DELETE` ir `UPDATE`. `INSERT` logiškai neturi kriterijų) su kriterijais link `Employee` entity. Prie šio objekto veliau bus pridedami papildomi kriterijai užklausai.<br>
Kitas objektas kurį sukuriam yra `Employee` objektas kaip užklausos dalis. tai reiškia šį objektą galima 
naudoti kai norima nustatyti kokiam stulpeliui kriterijus turi tikti. Stulpelį galima nurodyti iškviečiant `employeeRoot.get(<column_name>)`, tai yra objektas prie kurio yra kabinami kriterijai. Kuomet baigiama krterijų objetkas - jis paleidžiamas kaip užklausa naudojant `session.createQuery(criteriaQuery).getResultList()`;

### Caching

 `Caching` (kešavimas) yra mechanizmas kuris šiek tiek pagreitina užklausų rezultatų gavima. Tai yra sistema kuri egzistuoja tarp aplikacijos ir duombazės
 ir saugo naujausias užklausas siųstas iš kliento. Jei nepraeina nustatytas laikas rezultatai į tokias pačias užklausas yra gražinami tiesiai iš kešo ir nėra ištikrųjų siunčiami į duombazę taip sutaupant daug laiko.
 
 ![communication using hibernate](https://i.imgur.com/APtFgNZ.jpg) 
 
 ##### Kaip kešavimas veikia?
 
 Hibernate framework'e yra du kešavimo lygiai: 1st level cache ir 2nd level cache. Pirmas kešavimo lygis yra įjungtas automatiškai, antrą lygį reikia sukonfiguruoti. 
 Jei abi kešavimo sistemos įjungtos ENtity paieška veikia taip:
 * Kiekvieną kartą kai hibernate sesija bando užkrauti `Entity` pirmas dalykas kur ji žiuri tai yra pirmo lygio kešas kuris tvarkomas `Session` objekte.
 * Jei toks enity egzistuoja pirmame lygije tuomet tas jis yra tiesiog gražinamas kaip rezultatas.
 * Jei to enity nėra pirmo lygio keše tuomet yra žiūrima į antro lygio kešą.
 * Jei šis enity egzistuoja antro lygio keše - jis gražinamas kaip rezultatas. Tačiau prieš tai padarant objektas taip pat yra išaugotas pirmo lygio keše, kad nereikėtų daryti vėl paieškos antrą kartą.
 * Jei Enity neegzisutoja neiviename keše, užklausa į duombazę yra išsiunčiama. Gautas rezultatas yra įrašomas į abu kešo lygius. 
 * Antro lygio kešavimo sistema atnaujinta pakeistą objektą, jei pakeitimas buvo daromas naudojant hibernate sesijos api.
 * Jei įrašai duombazėje pasikteitė per laiką kol neišseko enity kešavimo nustatymas `timeToLiveSeconds` - kešavimo sistemai nėra kaip žinoti apie tą pasikeitimą ir tiesiog senas rezultatas bus gražintas iš kešo.
 
 If some user or process make changes directly in database, the there is no way that second level cache update itself until “timeToLiveSeconds” duration has passed for that cache region. In this case, it is good idea to invalidate whole cache and let hibernate build its cache once again. You can use below code snippet to invalidate whole hibernate second level cache.
 
 ##### 1. First-level cache
 
 Šis cache lygis yra `Session` objekto lygije. Keli dalykai žinoti apie 1st level cache:
  1. Hibernate First Level Cache yra įjungta automatiškai, tam konfiguracijos nereikia.
  2. Šis cache yra specifiškas sesijai. Pavyzdžiui dėl to jei du kartus iš eilės padarysim tą patį`session.find()`
  antrą kartą užklausa nebus išsiųsta į duombazę, tačiau jei naujoje sesijoje paleisim tą pačia užklausa iškarto - ji bus išsiųsta.
  3. Hibrenate First Level Cache glai turėti senų reikšmių.
  4. Galima naudoti šiuos sesijos metodus, kad manipuliuoti cache:
     * `evict(Object)` - išmesti tam tikrą objektą.
     * `clear()` - išvalyti viską.
     * `contains(Object)` - patikrina ar tam tikras objektas yra cache. Gražina boolean.
  5. Kadangi hibernate saugo *visas* užklausas darant daug pakeitimų, `bulk updates`, kai masiškai keičiami įrašai - būtina pravalyti cache dėl galimų atminties problemų.
 
 ##### 2. Second-level cache
 
 Antro lygio cache (kešas) galima sukonfiguruoti laikyti tik tam tikro `Entity` detales, užklausas, taip pat pasirūpinti
 , kad cache būtų išsaugotas `Session` objekte net ir išnaujo prisijungus.
 
 Prieš įjungiant second-level caching pirmiausia turim nutarti kelis dalykus.
 Pirmiausia reikia išsirinkti `Concurrency Strategy` tai yra algoritmas kurį pasirinksim manipuliuoti objektams egzistuojantiems keše:
 
 * read-only - tokio tipo kešas gerai tinka situacijoms kur dirbama su objektais kuriuos dažnai skaitom bet nemodifikuojam.
 Ši strategija yra ganėtinai paprasta ir greita.
 
 * nonstrict read-write - kaikurios aplikacijos tik kartais turi modifikuoti informaciją. Dėl to sitiuacijų kur du skirtingi sistemos komponentai
 bandytų pakeisti tą patį objektą vienu metu nelabai tikėtinos. Taigi tokiom situacijom tinka `nonstrict read-write` strategija.
   
 * read-write - tokia kešinmo strategija yra geriausia kai programa dažnai turi keisti informaciją. 
 
  
 * transactional - tokia strategija palaiko tam tikrus kešo privider'ius tokius kaip JBossTreeCache kuie turi savo distinktyviū informacijos kešinimo aspektų.
   
Taigi jei norėtume pridėti `Read-Write` strategijos second-level cache prie Entity objekto darytume taip:
 
 ```java
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee{<..>
```
Kitas dalykas kurį reikia išsirinkti yra `Cach Provider`. Tai yra klasė kuri pasirūpina pačiu "kešavimu" (chaching), tai yra saugo atmintyje užklausas ir gražina jų rezultatus jei nauja užklausa pakankamai nauja.
Yra keli cache providers:
* EHCache
* OSCache
* SwarmCache
* Jboss Cache

Ne kiekvienas provideris palaiko visas concurrency strategijas. Iš išvardintų tik EHCache ir OSCache palaiko `Read-Write`.
Taigi norit pridėti EHCache klasę į konfiguracijos (hibernate.cfg.xml) failą pridedam tokiąreikšmę:
```xml
<property name = "hibernate.cache.provider_class">
         org.hibernate.cache.EhCacheProvider
</property>
```
arba į hibernate.properties: `hibernate.cache.provider_class=org.hibernate.cache.EhCacheProvider`

Tokiu būdu mes įjungėm second-level caching valdoma EHCache providerio su Employee objektu skaitymu tvarkomo `read-write` concurrency strategy tipo algoritmu.

#####  Keletas pagrindinių funkcijų

* `session.flush()` - sinchronizuoja visus objektus užkešuotus pirmo lygio kešinge su duombaze.
* `session.evict(Object)` - išima objektą iš pirmo lygio kešo.
* `session.clear()` - išmeta visus objektus iš pirmo lygio kešo.
* `session.getSessionFactory().getCache().evictQueryRegion( "<region_name>" )`  išmeta užklausas pagal duota regiona
