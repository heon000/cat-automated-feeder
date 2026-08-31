package myPackage;
import java.util.*;

public class Cat_Feeding {
    
    private static final String DATA_FILE = "cat_data.txt"; //고양이 정보파일 경로
    
    static class Cat implements Serializable {
        private static final long serialVersionUID = 1L; //클래스 버전을 나타내는 ID
        
        private String name;
        private String breed;
        private double weight;
        private int activityLevel;
        private double dailyFood;
        private double feedingPortion;
        private List<String> feedingTimes = new ArrayList<>();
        
        public Cat(String name, String breed, double weight, int activityLevel) {
            this.name = name;
            this.breed = breed;
            this.weight = weight;
            this.activityLevel = activityLevel;
        }
        
        public String getName() { return name; }
        public String getBreed() { return breed; }
        public double getWeight() { return weight; }
        public int getActivityLevel() { return activityLevel; }
        
        public void setName(String name) { this.name = name; }
        public void setBreed(String breed) { this.breed = breed; }
        public void setWeight(double weight) { this.weight = weight; }
        public void setActivityLevel(int activityLevel) { this.activityLevel = activityLevel; }
        
        public double calculateFoodAmount() {
            double bmr = 70 * Math.pow(weight, 0.75); //기초대사량 계산
            double activityFactor = switch (activityLevel) {
                case 1 -> 1.2;
                case 2 -> 1.4;
                case 3 -> 1.6;
                default -> 1.0;
            };
            return bmr * activityFactor * 33/100;
        }
        
        public double getDailyFood() { return dailyFood; }
        public void setDailyFood(double dailyFood) { this.dailyFood = dailyFood; }
        
        public double getFeedingPortion() { return feedingPortion; }
        public void setFeedingPortion(double feedingPortion) { this.feedingPortion = feedingPortion; }
        
        public List<String> getFeedingTimes() { return feedingTimes; }
        public void addFeedingTime(String time) { feedingTimes.add(time); }
        public void clearFeedingTimes() { feedingTimes.clear(); }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cat cat = loadCatData(); //파일에서 고양이 정보 로드
        
        if (cat == null) { //입력된 고양이 정보 유무 확인 후 없다면 입력받기
            System.out.println("저장된 고양이 정보가 없습니다.");
            System.out.println("고양이 정보를 입력해주세요.");
            cat = addCatInfo(scanner);
            saveCatData(cat); //파일에 저장
        }
        
        System.out.println("고양이 자동 급식기");
        
        while(true) {
            System.out.println("[메뉴]");
            System.out.println("1. 고양이 정보 확인 및 수정");
            System.out.println("2. 적정 사료량 계산 및 배식시간 예약");
            System.out.println("3. 배식 실행");
            System.out.println("4. 종료");
            System.out.println("메뉴를 선택해주세요: ");
            int choice = scanner.nextInt();
            
            switch (choice) {
            case 1:
                CatInfo(cat, scanner);
                saveCatData(cat);
                break;
            case 2:
                calculateAndSchedule(cat, scanner);
                break;
            case 3:
                executeFeeding(cat);
                break;
            case 4:
                System.out.println("프로그램을 종료합니다");
                scanner.close();
                return;
            default :
                System.out.println("잘못된 입력입니다. ");
            }
        }
    }
    
    //0. 고양이 정보 입력 메서드
    private static Cat addCatInfo(Scanner scanner) {
        scanner.nextLine(); //입력 버퍼 비우기
        System.out.print("고양이 이름: ");
        String name = scanner.nextLine();
        System.out.print("품종: ");
        String breed = scanner.nextLine();
        System.out.print("몸무게(kg, 소수점 두 자리까지): ");
        double weight = scanner.nextInt();
        System.out.print("활동수준(1: 낮음, 2: 보통, 3: 높음): ");
        int activityLevel = scanner.nextInt();
        
        return new Cat(name, breed, weight, activityLevel);
    }
    
    //1. 고양이 정보 확인 및 수정 메서드
    private static void CatInfo(Cat cat, Scanner scanner) {
        System.out.println("\n고양이 정보");
        System.out.println("1. 이름: " + cat.getName());
        System.out.println("2. 품종: " + cat.getBreed());
        System.out.println("3. 몸무게: " + cat.getWeight());
        System.out.println("4. 활동수준: " + cat.getActivityLevel());
        System.out.println("5. 수정하지 않고 상위메뉴로 돌아가기");
        System.out.println("수정할 항목 번호를 입력하세요: ");
        int choice = scanner.nextInt();
        
        scanner.nextLine();
        switch (choice) {
        case 1:
            System.out.print("새 이름: ");
            cat.setName(scanner.nextLine());
            break;
        case 2:
            System.out.print("새 품종: ");
            cat.setBreed(scanner.nextLine());
            break;
        case 3:
            System.out.print("새 몸무게(kg, 소수점 두 자리까지: ");
            cat.setWeight(scanner.nextInt());
            break;
        case 4:
            System.out.print("새 활동 수준(1: 낮음, 2: 보통, 3: 높음): ");
            cat.setActivityLevel(scanner.nextInt());
            break;
        case 5:
            System.out.print("수정을 취소합니다.");
            return;
        default:
            System.out.println("잘못된 입력입니다.");
        }
        System.out.println("수정이 완료되었습니다.");
    }
    
    //2. 적정 사료량 계산 및 시간 예약 메서드
    private static void calculateAndSchedule(Cat cat, Scanner scanner) {
        double foodAmount = cat.calculateFoodAmount(); //적정 사료량 계산
        System.out.printf("\n%s(이)의 하루 적정 사료량: %.2fg\n", cat.getName(), foodAmount);
        
        int feedings; //배식 횟수 정하기
        while(true) {
            System.out.print("하루 배식 횟수를 정해주세요(최대 4회): ");
            feedings = scanner.nextInt();
            if (feedings < 1 || feedings > 4) {
                System.out.println("잘못된 입력입니다.");
            }
            else break;
        }
        
        scanner.nextLine();
        cat.clearFeedingTimes(); //기존예약 초기화
        for (int i = 0; i < feedings; i++) {
            System.out.printf("%d번째 배식 시간(1~24): ", i+1);
            String time = scanner.nextLine();
            cat.addFeedingTime(time);
        }
        
        double portion = foodAmount / feedings; //1회분 사료량 계산
        cat.setDailyFood(foodAmount);
        cat.setFeedingPortion(portion);
        System.out.printf("하루 %d번 한 번에 %.2fg씩 배식합니다.\n", feedings, portion);
        System.out.println("배식 시간 설정이 완료되었습니다.");
    }
    
    //(아두이노 명령 전송)
    /* private static void sendToArduino(String command) {
        try {
            SerialPort port = SerialPort.getCommPort("COM3"); //포트 번호에 따라 바뀜
            port.setComPortParameters(9600, 8, 1, 0); //Baud Rate 설정(통신속도 설정)
            port.openPort();
            
            port.getOutputStream().write((command + "\n").getBytes());
            port.getOutputStream().flush();
            port.closePort();
        }
    }
    */
    //3. 배식 실행 메서드
    private static void executeFeeding(Cat cat) {
        System.out.println("배식스케줄");
        for (String time : cat.getFeedingTimes()) {
            System.out.printf("- %s시에 %.2fg 배식 예정\n", time, cat.getFeedingPortion());
        }
        /* 아두이노 연동 예제
        for (String time : cat.getFeedingTimes()) {
            String command = "FEED" + cat.getName() + ":" + cat.getFeedingPortion();
            sendToArduino(command);
        }
        */
    }
    //4. 고양이 정보 파일 읽어오는 메서드
    private static Cat loadCatData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))){
            return (Cat) ois.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    //5. 고양이 정보 파일 저장 메서드
    private static void saveCatData(Cat cat) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))){
            oos.writeObject(cat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
