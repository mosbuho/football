package view;

public class PrintView {
    public static void printMainMenu() {
        System.out.println("-------------------------");
        System.out.println("|\tMINIFOOTBALL\t|");
        System.out.println("-------------------------");
        System.out.println("|\t1. 회원가입\t|");
        System.out.println("|\t2. 로그인\t|");
        System.out.println("|\t3. 종료\t\t|");
        System.out.println("-------------------------");
        System.out.print("메뉴 입력 : ");
    }

    public static void printLoggedInMainMenu() {
        System.out.println("-------------------------");
        System.out.println("|\tMINIFOOTBALL\t|");
        System.out.println("-------------------------");
        System.out.println("|\t1. 팀 목록\t|");
        System.out.println("|\t2. 선수 목록\t|");
        System.out.println("|\t3. 순위\t\t|");
        System.out.println("|\t4. 내 팀 관리\t|");
        System.out.println("|\t5. 레디\t\t|");
        System.out.println("|\t6. 플레이\t|");
        System.out.println("|\t7. 관리자\t|");
        System.out.println("|\t8. 종료\t\t|");
        System.out.println("-------------------------");
        System.out.print("메뉴 입력 : ");
    }

    public static void printMyClubMenu() {
        System.out.println("-------------------------");
        System.out.println("|\tMINIFOOTBALL\t|");
        System.out.println("-------------------------");
        System.out.println("|\t1. 내 팀 정보\t|");
        System.out.println("|\t2. 선수 판매\t|");
        System.out.println("|\t3. 선수 영입\t|");
        System.out.println("|\t4. 메인 메뉴\t|");
        System.out.println("-------------------------");
        System.out.print("메뉴 입력 : ");
    }

    public static void printAdminMenu() {
        System.out.println("-------------------------");
        System.out.println("|\tMINIFOOTBALL\t|");
        System.out.println("-------------------------");
        System.out.println("|\t1. 팀 생성\t|");
        System.out.println("|\t2. 팀 변경\t|");
        System.out.println("|\t3. 팀 삭제\t|");
        System.out.println("|\t4. 선수 생성\t|");
        System.out.println("|\t5. 선수 변경\t|");
        System.out.println("|\t6. 선수 삭제\t|");
        System.out.println("|\t7. 메인 메뉴\t|");
        System.out.println("-------------------------");
        System.out.print("메뉴 입력 : ");
    }
}