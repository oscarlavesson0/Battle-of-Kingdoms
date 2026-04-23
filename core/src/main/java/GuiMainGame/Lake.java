package GuiMainGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

    public class Lake extends StructureRenderer {

        private TextureRegion w00, w01, w02;
        private TextureRegion w10, w11, w12;
        private TextureRegion w20, w21, w22;

        public Lake(SpriteSheetLoader sheet) {

            layout = new int[][] {
                {120, 121, 122},
                {110, 111, 112},
                {100, 101, 102}
            };

            w00 = sheet.getTile(0, 2);
            w01 = sheet.getTile(0, 3);
            w02 = sheet.getTile(0, 4);

            w10 = sheet.getTile(1, 2);
            w11 = sheet.getTile(1, 3);
            w12 = sheet.getTile(1, 4);

            w20 = sheet.getTile(2, 2);
            w21 = sheet.getTile(2, 3);
            w22 = sheet.getTile(2, 4);
        }

        @Override
        public TextureRegion getTile(int id) {
            return switch (id) {
                case 100 -> w00;
                case 101 -> w01;
                case 102 -> w02;
                case 110 -> w10;
                case 111 -> w11;
                case 112 -> w12;
                case 120 -> w20;
                case 121 -> w21;
                case 122 -> w22;
                default -> null;
            };
        }
    }





