# Basic SQL queries

Šioje pamokoje bus pateikta informacija apie SQL užklausas.
Pavyzdžiuose bus naudojamas stalai kurie atrodo taip:
```
\------------------/
|     Person       |
--------------------
| ID | NAME  | AGE |
--------------------
| 1  | Jonas | 10  |
| 2  | Tomas | 53  |
| 3  | Egle  | 22  |
| 4  | Toma  | 33  |
| 5  | Ieva  | 22  |
/------------------\

\--------------------/
|     Salary       |
----------------------
| PERSON_ID |  PAY   |
----------------------
|     1     |  1000  |
|     2     |  2500  |
|     3     |     0  |
|     5     |  1234  |
/--------------------\

Salary(PERSON_ID) is FK for Person(ID);
```

### Operators

Operatoriai SQL veikia taip pat kaip ir daugumoje kitų vietų programavimo pasauly išskyrus loginius operatorius. SQL natively
palaiko daugiau loginių operatorių ir jie išreiškiami žodžiais, pcz: `AND`, `OR`, `LIKE`, `BETWEEN`. Naudojami `WHERE` išraiškoje
 
```sql
SELECT * FROM <table> WHERE <int_column> = <int_value> OR <some_other_string_column> = '<some_string_value>'
```
Pavyzdys: Išraiška norit rasti visus `Person` kur amžius lygu 10 arba `Person` vardas 'Egle' atrodytu taip:

```sql
SELECT * FROM person WHERE age = 10 OR name = 'Egle'
```
Grąžintų:
```
| 1  | Jonas | 10  |
| 4  | Egle  | 22  |
```

Daugiau info: https://www.tutorialspoint.com/sql/sql-operators.htm

### Wildcards

Wildcard yra išraiška kuri save teigiamai prilyginą betkokiai kitai išraiškai. Kaip matėt ji gali būti naudojama kuriant
naują duombazės vartotoją, tačiau dažnesnis panaudojimas yra `LIKE` statement'e apie kurį sužinosim daugiau kitame aprašyme.

* `%` - lygina bent vieną ar daugiau ženklų
* `_` - lygina vieną ženklą


### `LIKE` 

SQL loginis operatorius skirtas ieškoti apytikslėm reikšmėm. Dažniausiai naudojama `WHERE` išraiškoje.
```sql
SELECT * FROM <table> WHERE <column> LIKE 'XXXX%'
```
Pavyzdys: Su `Person` lentele tokia užklausa:

```sql
SELECT * FROM person WHERE name LIKE 'Tom%'
```
Grąžintų:
```
| 2  | Tomas | 53  |
| 4  | Toma  | 33  |
```
Nes išraiska `Tom%` tinka ir `Tomas` ir `Toma` stringam.

### Ordering

Duombazė gali grąžinti jau surikiuotus rezultatus. Tai padaryti galima naudojant `ORDER BY` išraišką kuri atrodo taip:

```sql
SELECT * FROM <table> ORDER BY <column> ASC; -- didėjimo tvarka.
SELECT * FROM <table> ORDER BY <column> DESC; -- mažėjimo tvarka.
```

Pavyzdys: 
```sql
SELECT * FROM person ORDER BY age DESC;
```
Grąžintų:
```
| 2 | Tomas | 53 |
| 4 | Toma  | 33 |
| 3 | Egle  | 22 |
| 5 | Ieva  | 22 |
| 1 | Jonas | 10 |
```

Tokiu būdu Java programoje išsaugant užklausa `ResultSet` klasėje pirmas įrašas būtų `Tomas` su indeksu 2

### `DISTINCT`

Renkantis įrašus su unikaliomis reikšmėmis galima utilizuoti raktažodį `DISTINCT`
```sql
SELECT DISTINCT <column> FROM <table>;
```
Pavyzdys:
```sql
SELECT DISTINCT age  FROM person;
```
Grąžintų:
```
| 10 |
| 53 |
| 22 |
| 33 |
```
Tai yra visos unikalios amžiaus reikšmės

### `GROUP BY`

Sąrašo užklausos (`SELECT` statement'as) rezultatus įmanoma grupuoti. Kuriant grupavimo užklausas reikia gerai suprasti 
rezultato išvestys (tai kas yra tarp `SELECT` ir `FROM`) turi gera sąryšį. Jei sąryšis neįmanomas, užklausa nesuveiks.

*Pavyzdys teisingo sąryšio*:
Person grupavimas pagal amžių. Toks grupavimas galėtų būti išreikštas išvedant amžių ir skaičių kiek tokios amžiaus žmonių yra.
Užklausos pavizdys žemiau.

*Pavyzdys neįmanomo sąryšio*: 
Jei prie paminėtos amžiaus išvesties pridėtume vardo (`name`) stulpelį duombazė nežinotu kaip susieti visus gautus vardus
prie vienos rezultato eilutės (kuri parodo amžių ir kiek to amžiaus žmonių yra dviejuose stulpeliuose) išvesties. Atsiminkit, 
kad vienas stulpelis gali laikyti tik vieną reikšmę.
```sql
SELECT name, age, COUNT(age) FROM person GROUP BY age; <-- klaidinga
```
Struktūra:
```sql
SELECT <column_1>, COUNT(<column_1>) FROM <table> GROUP BY <column_1>;
```

Pavyzdys: 
```sql
SELECT age, COUNT(age) FROM person GROUP BY age;
```
Grąžintų:
```
| 10 |	1 |
| 22 |	2 |
| 33 |	1 |
| 53 |	1 |
```

### `LIMIT`

`LIMIT` raktažodis leidžia limituoti rezultatų sąrašą taip, kad parodytu tik pirmus `N` įrašų, kur `N` yra nustatytas 
skaičius prie raktažodžio
 
 ```sql
SELECT * FROM LIMIT <N>
```

Pavyzdys:
```sql
select * from employee limit 3;
```
Grąžintų:
```
| 1  | Jonas | 10  |
| 2  | Tomas | 53  |
| 3  | Egle  | 22  |
```

## Advanced SQL queries

### Constraints

Constraints (apribojimai) yra taisyklės pritaikytos lentelių stulpeliams kurios limituoja nurodytas savybes. Pavyzdžiui
pridedant `NOT NULL` kuriant stulpelį - tam stulpelyje negali būti `null` reikšmių.

Kiti constraints kuriuos jau galimai žinot yra `AUTO_INCREMENT`, `PRIMARY KEY`, `UNIQUE` ir t.t.

Kaikurie apribojimai gali turėti nuosavą pavadinimą, pavyzdžiui `FOREIGN KEY`. Standartinė praktika yra pavadinti šiuos 
apribojimus pagal pačio apribojimo trumpinimą (`FOREIGN KEY` -> FK), pridėti `_` ir trumpai nusakyti koks tai apribojimas. 
Pavyzdžiui `fk_employee_Id`. Tai yra reikalinga pagrinde tam, kad klaidų žinutės būtų aiškesnės. Taip pat tai padaro
tvarkingesnė duombazė, lengviau ieškoti reikiamų apribojimų naudojant `LIKE` raktažodį ir t.t.

MySQL pažiūrėti kokie apribojimai egzistuoja galima su šia užklausa:
```sql
select * from information_schema.table_constraints where constraint_schema = 'playground';  -- <- "playground" - duombazės pavadinimas kur ieškote apribojimų.
```

O jei norima šiek tiek daugiau detalių, galima taip:
```sql
SELECT tc.constraint_schema,tc.constraint_name,tc.table_name,tc.constraint_type,kcu.table_name,kcu.column_name,kcu.referenced_table_name,kcu.referenced_column_name,rc.update_rule,rc.delete_rule

FROM information_schema.table_constraints tc

       inner JOIN information_schema.key_column_usage kcu
                  ON tc.constraint_catalog = kcu.constraint_catalog
                    AND tc.constraint_schema = kcu.constraint_schema
                    AND tc.constraint_name = kcu.constraint_name
                    AND tc.table_name = kcu.table_name

       LEFT JOIN information_schema.referential_constraints rc
                 ON tc.constraint_catalog = rc.constraint_catalog
                   AND tc.constraint_schema = rc.constraint_schema
                   AND tc.constraint_name = rc.constraint_name
                   AND tc.table_name = rc.table_name

WHERE tc.constraint_schema = 'playground' -- <- "playground" - duombazės pavadinimas kur ieškote apribojimų.
```

### Alias

Alias yra alternatyvus pavadinimas kažkokiai reikšmei: stulpeliui, stalui, funkcijai ar betkokiai išraiškai.
Alias'ai leižia kurti aiškiasnius rezultatų setus ir aiškiasnes užklausas. Pavyzdžiui galima funkcija `COUNT(age)` 
pavadinti tiesiog `skaicius` (ar `number`) ir po to tą pačią išraiška naudoti toliau užklausoje.

```sql
SELECT <alias_name.column1> FROM <table_name> AS <alias_name>
```

Pavyzdys:
```sql
SELECT age, COUNT(age) AS number FROM person GROUP BY age ORDER BY number;
```
Grąžintų:
```
\----------------/
|     Person     |
------------------
|  age  | number |
------------------
|  10   |    1   |
|  53   |    1   |
|  33   |    1   |
|  22   |    2   |
/----------------\
```

Čia išraiškai `COUNT(age)` buvo duotas alias `number` ir toliau tą patį alias galima naudoti duomenų rikiavimui `ORDER BY` 
išraiškoje. Taip pat atkreipkite dėmesį į rezultato stulpelio pavadinimą kuris taip pat yra `number`.

### Queries within queries or Sub querying

SQL įmanoma kurti užklausas užklausose. Esminė taisyklė yra ta, kad reikšmė kurią gražina užklausa turi būti suvokiama 
loginėje išraiškoje kurioje ji naudojama.

Pavyzdys: Norint rasti žmogaus vardą kuris turi didžiausią atlyginimą utilizuojant dvi lenteles, `Person` ir `Salary`, galima daryti taip:
```sql
SELECT name FROM person where id = (SELECT PERSON_ID FROM SALARY ORDER BY SALARY DESC LIMIT 1);
```
Mes žinom, kad `PERSON_ID` stulpelis letelėje `SALARY` reprezentuoja `PERSON` lentelės stulpelį `ID`. Tai yra todėl, kad `PERSON_ID` yra `FOREIGN KEY` į `Person(id)`.
Todėl mes sukuriam pirmą užklausą: 
```sql 
SELECT PERSON_ID FROM SALARY ORDER BY SALARY DESC LIMIT 1;
```
kad rasti id žmogaus kuris turi didžiausią atlyginimą. Atkreipkite dėmsį, kad ši užklausa gražiną vieną stulpelį su viena 
reikšmę. Tai darom todėl, kad lygybės ženklas (` where id =`) gali lyginti tik su viena reikšme (kaip ir `>`, `<`, `LIKE` ir t.t.). Taigi, gavę šią vieną
reikšmę mes galim surasti įrašą kitoje lentelėje naudodami užklausą užklausoje.

Užklausą užklausoje taip pat galima naudoti ir su keliomis reikšmėmis, tačiau reikia naudoti kitokį loginį operatorių, pavyzdžiui `IN` - 
reikšmės lyginimas su aibe, kitaip tariant jei reikšmė egzistuoja duotoje aibėje loginė reikšmė gražins `true`, kitu atvėju `false` .
Taigi jei modifikuojam buvusę užklausą, kad ji grąžintų stulpelį su keliais įrašais:
```sql
SELECT PERSON_ID FROM SALARY ORDER BY SALARY DESC;
```
rezultatu:
```
|     2     |
|     5     |
|     1     |
|     3     | 
```
ir pakeičiam `=` į `IN`: 
```sql
SELECT name FROM person where id IN (SELECT PERSON_ID FROM SALARY ORDER BY SALARY DESC);
```
rezultatas būtų:
```sql
| Tomas |
| Ieva  |
| Jonas |
| Egle  |
```
`Toma` nebūtų rezultate nes `Salary` stalas neturi jos įrašo (`PERSON_ID` kuris būtų 4).

### Joins

Viena iš stipriausių SQL funkcijų. `Join`'as (sujungimas) leidžia pasirinkti reikšmes iš kelių skirtingų lentelių duombazėj ir su jomis dirbti.
`Join`'ai veikia tarp lentelių kurios gali turėti saryšį, nors constraint'as (apribojimas) nebūtinai turi egzistuoti.

Sintaksė:

```sql
SELECT * FROM <table1> as <t1>
    <join_type> JOIN <table2> as <t2> ON <t2>.<column> = <t1>.<column> 
```
`ON` raktažodis rodo kokiu stulpeliu sujungimas turi būti paremtas. Kitaip tariant `<t2>.<column>` ir `<t1>.<column>` turi
 reprezentuoti tą pačią reikšmę skirtinguose staluose, kad ši užklausa turėtu logikos. Galima sujunginėti kelis stalus.

Duombazės struktura naudoja savokas `kairė` ir `dešinė` lentelė.  `Kairė` - lentelė prie kurio jungiama (`SELECT * FROM <kairė_lentelė <..>`).
`Dešinė` - ta kuri yra jungiama (`<..> JOIN <dešinė_lentelė>`). Vizualizacija galite pamatyti apačioje.

Egzistuoja skirtingų tipų `JOIN`'ai:<br>
   * `INNER JOIN` - gražina tik stulpelius kurie turi visas reikšmes<br>
   * `LEFT JOIN` - gražina visus stulpelius iš kairės lentelės net jei jos neturi reikšmės dešinėje lentelėje<br>
   * `RIGHT JOIN` - gražina visus stulpelius iš dešinės lentelės net jei jos neturi reikšmės kairėje lentelėje<br>
   * `FULL JOIN` - gražina stulpelius jei yra reikšmės bent viename stulpelyje. MySQL nepalaiko šitos užklausos<br>
   * `CARTESIAN/CROSS JOIN` - Tai yra kiekvienos reišmės prijungimas kairėje lentelėje prie kiekvienos reikšmės dešinėje lentelėje. 
      Pvz jei viena letelė turi 10 įrašų ir kita 100, rezultate bus 1000 įrašų. (MySQL naudoja `CROSS` raktažodį)
   * Self join - nėra raktažodžio lentelės. Tai yra lentelės prijungimas prie savęs. Turi būti naudojami alias (žiūrėti Alias aprašymą)<br>.


![Different types of joins](https://i.imgur.com/b3o1R1j.png)

### `UNION`

`UNION` raktažodis yra naudojamas kai norima sujungti rezultatus iš kelių `SELECT` užklausų ir nenorima gauti duplikuotų reikšmių.
Utilizuoti `UNION` išraišką galima jei naudojamos `SELECT` užklausos turi:
 * Vienodą sakičių rezultatų
 * Vienodą skaičių stulpelių
 * Tą patį datos tipą
 * Tą patį eiliškumą
 
Sintaksė:
```sql
SELECT <column_1>, <column_2> from <table_1>
UNION                                   
SELECT <column_1>, <column_2> from <table_1>;
```
 
Pavizdys:
```sql
select * from person where age < 22  --<- užklausa 1
union
select * from person where AGE > 33; --<- užklausa 2
```
Grąžintų:
```
| 1 | Jonas |	10 |
| 2 | Tomas |	53 |
```
nes pirmos užklausos rezultatai yra sujungiami su antros užklausos rezultatais.
