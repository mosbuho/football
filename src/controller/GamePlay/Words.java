package controller.GamePlay;

import java.util.Random;

public class Words {
   public static Random random = new Random();

   public static String getPlayerWords() {
      String[] player_words = {
            "슈팅 스타",
            "드리블 마에스트로",
            "골키퍼 호그",
            "수비의 벽",
            "미드필더 매직",
            "플레이 메이커",
            "골 머신",
            "헤딩 스페셜리스트",
            "볼 컨트롤러",
            "프리킥 마에스트로",
            "슈퍼 서브",
            "크로스 마스터",
            "허슬러",
            "팀 캡틴"
      };
      return player_words[random.nextInt(player_words.length)];
   }

   public static String getPasWords() {
      String[] pas_words = {
            "필드를 가로지르는 정밀 스루 패스",
            "공중을 지배하는 높은 헤딩 패스",
            "순식간에 이뤄지는 빠른 원투 패스",
            "눈에 띄지 않는 은밀한 오프더볼 패스",
            "수비수를 현혹하는 매혹적인 드리블 패스",
            "수비 라인을 무너뜨리는 강력한 원투패스",
            "골대를 정조준하는 날카로운 드리븐 패스",
            "상황 판단력을 빛내는 즉흥적 오너십 패스",
            "결정적인 순간의 완벽한 타이밍 원투패스",
            "팀 플레이를 극대화하는 윙퍼스 패스"
      };
      return pas_words[random.nextInt(pas_words.length)];
   }

   public static String getDefWords() {
      String[] def_words = {
            "철벽 같은 솔리드 블로킹",
            "상대 팀의 공격 중심을 차단하는 포인트 디펜스",
            "공격적인 강력한 프레스",
            "그림자처럼 따라붙는 쉐도잉",
            "주요 공격수를 제압하는 마크맨십",
            "공을 되찾는 예리한 루스볼 리커버리",
            "전장을 꿰뚫는 필드 비전",
            "수비진을 지휘하는 리더쉽",
            "정교한 수비 포지셔닝",
            "공을 차지하는 볼 윈닝",
            "적절한 순간의 결정적인 체크",
            "위기를 예방하는 프리벤트 디펜스",
            "시기적절한 타이밍 수비",
            "공격의 날을 꺾는 스파이크 블로킹",
            "리바운드를 지배하는 컨트롤"
      };
      return def_words[random.nextInt(def_words.length)];
   }

   public static String getShoWords() {
      String[] sho_words = {
            "경이로운 아크로바틱 슛",
            "힘찬 파워 슛",
            "정교함을 자랑하는 테크니컬 슛",
            "직관적인 체감 슈팅",
            "결정적인 순간의 킬러 슛",
            "장거리를 가르는 런칭 슛",
            "수비의 벽을 뚫는 파괴적인 슛",
            "연습의 결실, 리본 슛",
            "하늘을 나는 에어본 슛",
            "비어 있는 골대를 찾아내는 오픈 넷 슛",
            "승리를 부르는 챔피언스 리그 슛",
            "포스트를 울리는 강력한 슛",
            "하늘 높이 솟는 스파이더맨 슈팅",
            "아름다운 바이시클 킥"
      };
      return sho_words[random.nextInt(sho_words.length)];
   }

   public static String getGoalWords() {
      String[] goal_words = {
            "화려한 골을 장식합니다.",
            "강력하게 골망을 흔듭니다.",
            "골망을 찌릅니다.",
            "정확한 골로 이어집니다.",
            "골 지역에 꽂혔습니다.",
            "골망을 찢습니다.",
            "골 지점을 강타합니다.",
            "골망을 향해 비상합니다.",
            "네트를 흔듭니다."
      };
      return goal_words[random.nextInt(goal_words.length)];
   }
}
