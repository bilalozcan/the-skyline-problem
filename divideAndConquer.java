import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
/* Verilen csv dosyasından başlangıç noktası, genişlik ve yükseklik verilerini kullanarak bir dikdörtgenin
 * 2 dikey doğrusunu ve x ekseni ile çakışık olmayan yatay doğrusunu alarak ve bunu her dikdörtgen için yaparak
 * dikey doğrular ve de yatay doğular diye ArrayList'lere kaydeder.(En son yatay doğrulara x eksenini de ekler)
 * Daha sonra her yatay doğruyu her dikey doğru ile kesişiyor mu diy ekontrol edip eğer kesişim noktası var ise
 * bunu bir kesişimNoktaları ArrayList'in de tutup bu kesişimNoktaları ArrayList'indeki noktalar bizim işimize yarayan kesişim noktasaları mı
 * onu kontrol edip işimize yarayanları kesişimNoktları2 ArrayList'ine alır.Bu noktaları sıralar. Bu noktalar arasında belirli şartlara göre
 * ilerleyip şekli çevreleyen ipi bulur.
 * n adet Dikdörtgen var ise 2n adet dikey doğru,
 *                        n + 1 adet yatay doğru vardır. (x eksenini de eklediğimiz için +1 ekleniyor).
 */
/* Main de sırasıyla "dosyadanOku", "kesisimNoktalarıBulma", "kesisimNoktalarıKontrol", "siralama" ve "uzunlukBul" fonksiyonları çağırıldığı için ve
 * bunların çalışma zamanları fonksiyon fonksiyon olarak hesaplandığı için algoritma çalışma zamanı: O(n) + O(n^2) + O(n^3) + O(n^2) + O(n^2) ==> O(n^3)'dür.  */
public class divideAndConquer {
    //Bir doğru parçasını nesne olarak tutmamızı sağlayan Doğru Class'ı 
    public static class Dogru {
        public final int x1, y1, x2, y2; //x1 ve y1 Doğrunun Başlangıç Noktası, x2 ve y2 Doğrunun bitiş noktası
        public Dogru(final int x1, final int y1, final int x2, final int y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }
    }
    //Bir Koordinat Noktasını  nesne olarak tutmamızı sağlayan Nokta Class'ı 
    public static class Nokta {
        public final int x1, y1; // Koordinat Noktasının x ve y noktaları
        public Nokta(final int x1, final int y1) {
            this.x1 = x1;
            this.y1 = y1;
        }
    }
    //Bütün yatay ve dikey doğruları birbiri ile karşılaştırıp bellirli şartları sağlıyorsa o doğruların
    //bir kesişim noktaları olduğunu bulup bir ArrayListe kaydeden fonksiyondur.
    //Çalışma Zamanı:
    //  İlk for 2n, ikinici for n+1 adet döner. 2n*(n+1) = 2n^2+2n ==> O(n^2) 
    public static ArrayList<Nokta> kesisimNoktalarıBulma(final ArrayList<Dogru> dikDogrular, final ArrayList<Dogru> yatayDogrular) {
        final ArrayList<Nokta> kesisimNoktaları = new ArrayList<Nokta>();
        for (int i = 0; i < dikDogrular.size(); i++) {
            for (int j = 0; j < yatayDogrular.size(); j++) {
                if(yatayDogrular.get(j).x1 <= dikDogrular.get(i).x1 && dikDogrular.get(i).x1 <= yatayDogrular.get(j).x2
                && yatayDogrular.get(j).y1 <= dikDogrular.get(i).y2){
                    kesisimNoktaları.add(new Nokta(dikDogrular.get(i).x1, yatayDogrular.get(j).y1));
                }
            }
        }
        return kesisimNoktaları;
    }
    //Bütün kesişimNoktalarını, yatay doğrular ile karşılaştırıp bellirli şartları sağlıyorsa o noktaların
    //işimize yarayn kesişim noktaları olduğunu tespit edip  bir ArrayListe kaydeden fonksiyondur.
    //Çalışma Zamanı:
    //  İlk for (eğer bütün yatay doğruların bütün dikey doğrular ile kesiştiğini varsayarsak) 2n*(n+1) = 2n^2+2n adet nokta,
    //  ikinici for ise n+1 adet döner. 
    // (2n^2 + 2n)*(n+1) = 2n^3 + 2n^2 + 2n^2 + 2n ==> O(n^3)
    public static ArrayList<Nokta> kesisimNoktalarıKontrol(final ArrayList<Nokta> kesisimNoktaları, final ArrayList<Dogru> yatayDogrular){
        final ArrayList<Nokta> kesisimNoktaları2 = new ArrayList<Nokta>();
        boolean kontrol = false;
        for (int i = 0; i < kesisimNoktaları.size(); i++) {
            for (int j = 0; j < yatayDogrular.size(); j++) {
                kontrol = yatayDogrular.get(j).y1 > kesisimNoktaları.get(i).y1 && yatayDogrular.get(j).x1 < kesisimNoktaları.get(i).x1 &&
                kesisimNoktaları.get(i).x1 < yatayDogrular.get(j).x2;
                if(kontrol){
                    break;
                }
            }
            if(kontrol) {
                continue;
            }else{
                kesisimNoktaları2.add(kesisimNoktaları.get(i));
            }
        }
        return kesisimNoktaları2;
    }
    //Kesişim Noktlarını x noktalarına göre sıralayan fonksiyon
    //Çalışma Zamanı:
    //  Insertion Sort algoritması çalışma zamanı n^2 olduğu için ==> O(n^2)
    static void siralama(final ArrayList<Nokta> kesisimNoktaları) {
        for (int i = 1; i < kesisimNoktaları.size(); ++i) {
            Nokta temp = kesisimNoktaları.get(i);
            int j = i - 1;
            while (j >= 0 && kesisimNoktaları.get(j).x1 >= temp.x1) {
                kesisimNoktaları.set(j + 1, kesisimNoktaları.get(j));
                j = j - 1;
            }
            kesisimNoktaları.set(j + 1, temp);
        }
    }
    //Kesişim noktalarında iki noktanın x değeri eşit ise y de; y noktaları eşit ise x de ilerleyen ve ip uzunluğuna ekleyen fonksiyon
    //Çalışma Zamanı:
    //  for (eğer bütün yatay doğruların bütün dikey doğrular ile kesiştiğini varsayarsak) 2n*(n+1) = 2n^2+2n adet nokta,
    //                                                                                               ==> O(n^2)
    static int uzunlukBul(final ArrayList<Nokta> kesisimNoktaları){
        int toplam = 0;
        for(int i = 0; i < kesisimNoktaları.size(); ++i){
            int j = i + 1;
            if(j < kesisimNoktaları.size()){
                if(kesisimNoktaları.get(i).x1 == kesisimNoktaları.get(j).x1){
                    toplam +=Math.abs(kesisimNoktaları.get(j).y1 - kesisimNoktaları.get(i).y1);
                } else {
                    toplam += Math.abs(kesisimNoktaları.get(j).x1 - kesisimNoktaları.get(i).x1);
                }
            }
        }
        return toplam;
    }
    //CSV Dosyasından Okuyup Onları Doğru olarak (yatay ve dikey doğru) kaydeden fonksiyon
    //Çalışma Zamanı olarak while döngüsünde Satır Satır ayırdığı için n adet dikdörtgen var ise 
    //while döngüsü n kere, içerideki for döngüsü ise ',' e göre split edilen dizinin herbir elemanını
    //aldığı için 3 kere döner. ÇALIŞMA ZAMANI: n * 3 = 3n ==> O(n)'dir.
    static void dosyadanOku(final ArrayList<Dogru> dikDogrular, final ArrayList<Dogru> yatayDogrular){
        try {
            File inputFile = new File("degerler.csv");
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String str[] = line.split(",",3);
                int b[] = new int[3];
                int i = 0;
                for (String a : str){
                    b[i] = Integer.parseInt(a);
                    i++;
                }
                dikDogrular.add(new Dogru(b[2], 0, b[2], b[1]));
                dikDogrular.add(new Dogru(b[2] + b[0], 0, b[2] + b[0], b[1]));
                yatayDogrular.add(new Dogru(b[2], b[1], b[2] + b[0], b[1]));
            }
            yatayDogrular.add(new Dogru(0, 0, 425, 0));
            reader.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(final String[] args) {
      
        final ArrayList<Dogru> dikDogrular = new ArrayList<Dogru>();
        final ArrayList<Dogru> yatayDogrular = new ArrayList<Dogru>();
        ArrayList<Nokta> kesisimNoktaları = new ArrayList<Nokta>();
        ArrayList<Nokta> kesisimNoktaları2 = new ArrayList<Nokta>();
        dosyadanOku(dikDogrular, yatayDogrular);
        kesisimNoktaları = kesisimNoktalarıBulma(dikDogrular, yatayDogrular);
        kesisimNoktaları2 = kesisimNoktalarıKontrol(kesisimNoktaları, yatayDogrular);
        siralama(kesisimNoktaları2);
        System.out.println("IP UZUNLUGU : " + uzunlukBul(kesisimNoktaları2));
        
	}
}