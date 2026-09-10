

/*
  -> represent the main game window.
  -> responsible only for displaying the user interface.
  -> forwarding user actions to the game logic.
*/








package ui;
import combat.*;
import inventory.*;
import npc.NPC;
import npc.NPCProfile;
import player.Player;
import story.Choice;
import story.ChoiceType;
import story.StoryManager;
import story.StoryScene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class GameWindow extends JFrame {
    private static final String GAME_TITTLE = "LORD OF REALMS";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    private static final Color BACKGROUND_COLOR = Color.decode("#323437");
    private static final Color PANEL_COLOR = Color.decode("#2C2E31");
    private static final Color TEXT_COLOR = Color.decode("#D1D0C5");
    private static final Color SECONDARY_COLOR = Color.decode("#646669");
    private static final Color ACCENT_COLOR = Color.decode("#E2B714");

    private JLabel statusTitle;
    private JLabel inventoryTitle;
    private JLabel storyTitle;
    private Timer storyTimer;


    private JPanel mainPanel;

    //Status  panel...
    private JPanel topPanel;
    private JPanel inventoryPanel;
    private JTextArea inventoryTextArea;
    private JScrollPane inventoryScrollPane;
    private JPanel statusPanel;
    private JLabel nameLabel;
    private JLabel realmLabel;
    private JLabel hpLabel;
    private JLabel qiLabel;
    private JLabel expLabel;
    private JLabel defenseLabel;
    private JLabel goldLabel;


    //story panel...
    private JPanel storyPanel;
    private JTextArea storyTextArea;
    private JScrollPane storyScrollPane;


    //Choice Panel...
    private JPanel choicePanel;
    private JButton choice1Button;
    private JButton choice2Button;
    private JButton choice3Button;
    private JButton choice4Button;
    private JButton combatBackButton;
    private JButton combatContinueButton;
    private JButton skillsButton;
    private JButton breakthroughButton;
    private int currentChoiceCount;




    private Player player;
    private StoryManager storyManager;
    private CombatManager combatManager;
    private Choice combatChoice;
    private boolean inCombat;
    private boolean selectingCombatItem;
    private boolean selectingCombatAttack;
    private List<Item> combatItems;
    private List<Item> combatWeapons;
    private boolean combatResult;
    private boolean selectingSkillScroll;
    private List<Item> skillScrolls = new ArrayList<>();
    private List<Skill> combatSkills = new ArrayList<>();
    private boolean selectingCombatSkill;
    private boolean viewingNPCProfile = false;

    private boolean inNPC = false;
    private NPC currentNPC;
    private Choice npcChoice;
    private boolean selectingTradeItem = false;





    public GameWindow(Player player, StoryManager storyManager){
        this.player = player;
        this.storyManager = storyManager;
        this.storyManager.startStory(1);

        initialize();
        updatePlayerInfo();
        updateInventory();
        updateScene();// Actual window Frame
    }

    // Details of window
    public void initialize(){
        configureWindow();
        createComponents();
        layoutComponents();
        applyTheme();
        registerListeners();
    }


     private void registerListeners() {

          choice1Button.addActionListener( e-> handleButtonClick(0));

          choice2Button.addActionListener(e -> handleButtonClick(1));

          choice3Button.addActionListener( e-> handleButtonClick(2));

          choice4Button.addActionListener( e-> handleButtonClick(3));

          combatBackButton.addActionListener(e -> {
              if(inCombat){
                  showMainCombatActions();
              }
              else if(viewingNPCProfile){
                  viewingNPCProfile = false;
                  showNPCMenu();
              }
              else if(inNPC){
                  leaveNPC();
              }
          });

          combatContinueButton.addActionListener(e -> handleCombatContinue());

          skillsButton.addActionListener(e -> showSkillScrolls());

          breakthroughButton.addActionListener(e -> handleBreakthrough());

      }


      // it gives player in combat or story
      private void handleButtonClick(int buttonIndex) {

        if(inCombat){

            if(selectingCombatSkill){
                handleCombatSkill(buttonIndex);
            } else {
                handleCombatAction(buttonIndex);
            }
        } else if(selectingSkillScroll){

            handleSkillScroll(buttonIndex);
        }

        else if(selectingTradeItem){
            handleNPCTrade(buttonIndex);
        }

        else if(inNPC){
            handleNPCAction(buttonIndex);

        } else{

           handleChoice(buttonIndex);
      }
    }


    // layouts,fonts,colors,borders, sizes etc.......
    private void layoutComponents() {
        mainPanel.setLayout(new BorderLayout());
        //  mainPanel.add(statusPanel, BorderLayout.NORTH);


        topPanel.setLayout(new GridLayout(1,2));
        topPanel.add(statusPanel);
        topPanel.add(inventoryPanel);
        topPanel.setPreferredSize(new Dimension(1200, 180));

        mainPanel.add(topPanel,BorderLayout.NORTH);
        mainPanel.add(storyPanel, BorderLayout.CENTER);
        mainPanel.add(choicePanel, BorderLayout.SOUTH);
        add(mainPanel);




       // statusPanel.setBackground(Color.RED);
       // storyPanel.setBackground(Color.BLUE);
       // choicePanel.setBackground(Color.GREEN);

        // status panel...

        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.add(inventoryScrollPane, BorderLayout.CENTER);

        statusPanel.setLayout(
                new BoxLayout(statusPanel,BoxLayout.Y_AXIS)
        );

        statusPanel.add(nameLabel);
        statusPanel.add(Box.createVerticalStrut(2));

        statusPanel.add(realmLabel);
        statusPanel.add(Box.createVerticalStrut(2));

        statusPanel.add(hpLabel);
        statusPanel.add(Box.createVerticalStrut(2));

        statusPanel.add(qiLabel);
        statusPanel.add(Box.createVerticalStrut(2));

        statusPanel.add(expLabel);
        statusPanel.add(Box.createVerticalStrut(2));

        statusPanel.add(defenseLabel);
        statusPanel.add(Box.createVerticalStrut(2));

        statusPanel.add(goldLabel);


        Font statusFont = new Font("SansSerif", Font.PLAIN,16);
        nameLabel.setFont(statusFont);
        realmLabel.setFont(statusFont);
        hpLabel.setFont(statusFont);
        qiLabel.setFont(statusFont);
        expLabel.setFont(statusFont);
        defenseLabel.setFont(statusFont);
        goldLabel.setFont(statusFont);

        // inventoryLabel.setFont(new Font("font",Font.PLAIN,16));
        // statusPanel.add(inventoryLabel);

        // story panel...
        storyPanel.setLayout(new BorderLayout());
        storyPanel.add(storyScrollPane,BorderLayout.CENTER);

        // choice panel....
        choicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        choicePanel.add(choice1Button);
        choicePanel.add(choice2Button);
        choicePanel.add(choice3Button);
        choicePanel.add(choice4Button);
        choicePanel.add(combatBackButton);
        choicePanel.add(combatContinueButton);
        choicePanel.add(skillsButton);
        choicePanel.add(breakthroughButton);






        choicePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));




    }

    private void applyTheme() {


      statusPanel.setBorder(
              BorderFactory.createTitledBorder(
                      BorderFactory.createCompoundBorder(
                              BorderFactory.createLineBorder(SECONDARY_COLOR),
                              BorderFactory.createEmptyBorder(10,10,10,10)
                      ),
                      "STATUS",
                      0,
                      0,
                      null,
                      ACCENT_COLOR
              )
      );

        inventoryPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(SECONDARY_COLOR),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ),
                        "INVENTORY",
                        0,
                        0,
                        null,
                        ACCENT_COLOR
                )
        );

        storyPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(SECONDARY_COLOR),
                        "STORY",
                        0,
                        0,
                        null,
                        ACCENT_COLOR
                )
        );

        // Main background
        mainPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBackground(BACKGROUND_COLOR);
        storyPanel.setBackground(BACKGROUND_COLOR);
        choicePanel.setBackground(BACKGROUND_COLOR);

        // Status and inventory
        statusPanel.setBackground(PANEL_COLOR);
        inventoryPanel.setBackground(PANEL_COLOR);

        // Text areas
        inventoryTextArea.setBackground(PANEL_COLOR);
        inventoryTextArea.setForeground(TEXT_COLOR);

        storyTextArea.setBackground(BACKGROUND_COLOR);
        storyTextArea.setForeground(TEXT_COLOR);

        // Labels
        nameLabel.setForeground(TEXT_COLOR);
        realmLabel.setForeground(TEXT_COLOR);
        hpLabel.setForeground(TEXT_COLOR);
        qiLabel.setForeground(TEXT_COLOR);
        expLabel.setForeground(TEXT_COLOR);
        defenseLabel.setForeground(TEXT_COLOR);
        goldLabel.setForeground(TEXT_COLOR);

        statusTitle.setForeground(ACCENT_COLOR);
        inventoryTitle.setForeground(ACCENT_COLOR);
        storyTitle.setForeground(ACCENT_COLOR);

        // Buttons
        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button,
                combatBackButton,
                combatContinueButton,
                skillsButton,
                breakthroughButton
        };

        for (JButton button : buttons) {
            button.setBackground(PANEL_COLOR);
            button.setForeground(TEXT_COLOR);
            button.setFocusPainted(false);
            button.setOpaque(true);
            addHoverEffect(button);

          /*  button.setBorder(
                    BorderFactory.createLineBorder(SECONDARY_COLOR, 3)
            );*/


          //  button.setBackground(new Color(210, 210, 210));
          //  button.setForeground(new Color(35,35,35));
           // button.setFont(new Font("SansSerif", Font.BOLD, 16));
        }



    }


    private void addHoverEffect(JButton button){
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e){
                button.setBackground(new Color(60, 60, 60));
            }

            @Override
            public void mouseExited(MouseEvent e){
                button.setBackground(PANEL_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(45, 45, 45));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(60, 60, 60));
            }
        });
    }


    // all my panel, buttons, labels, text area etc......
    private void createComponents() {
        mainPanel = new JPanel();


        //status panel...
        statusPanel = new JPanel();
        nameLabel = new JLabel();
        realmLabel = new JLabel();
        hpLabel = new JLabel();
        qiLabel = new JLabel();
        expLabel = new JLabel();
        defenseLabel = new JLabel();
        goldLabel = new JLabel();

        statusTitle = new JLabel("STATUS");
        inventoryTitle = new JLabel("INVENTORY");
        storyTitle = new JLabel("STORY");

        topPanel = new JPanel();
        inventoryPanel = new JPanel();

        inventoryTextArea = new JTextArea();
        inventoryTextArea.setEditable(false);
        inventoryTextArea.setLineWrap(true);
        inventoryTextArea.setWrapStyleWord(true);
        inventoryTextArea.setMargin(new Insets(10, 15, 10, 15));

        inventoryScrollPane = new JScrollPane(inventoryTextArea);
        inventoryScrollPane.setBorder(null);

        //story panel....
        storyPanel = new JPanel();
        storyTextArea = new JTextArea();
        inventoryTextArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        storyTextArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        storyTextArea.setMargin(new Insets(15,15,15,15));


        storyScrollPane = new JScrollPane(storyTextArea);
        storyScrollPane.setBorder(null);
        storyTextArea.setEditable(false);
        storyTextArea.setLineWrap(true);
        storyTextArea.setWrapStyleWord(true);

        //choice panel...

        choicePanel = new JPanel();
        choice1Button = new JButton();
        choice2Button = new JButton();
        choice3Button = new JButton();
        choice4Button = new JButton();
        combatBackButton = new JButton("Back");
        combatBackButton.setVisible(false);
        combatContinueButton = new JButton("Continue");
        combatContinueButton.setVisible(false);
        skillsButton = new JButton("Skills");
        skillsButton.setVisible(false);
        breakthroughButton = new JButton("Breakthrough");
        breakthroughButton.setVisible(false);


        Insets buttonMargin = new Insets(2, 15, 2, 15);

        choice1Button.setMargin(buttonMargin);
        choice2Button.setMargin(buttonMargin);
        choice3Button.setMargin(buttonMargin);
        choice4Button.setMargin(buttonMargin);



        choice1Button.setFocusPainted(false);
        choice2Button.setFocusPainted(false);
        choice3Button.setFocusPainted(false);
        choice4Button.setFocusPainted(false);

        Font choiceFont = new Font("SansSerif", Font.PLAIN, 15);

        choice1Button.setFont(choiceFont);
        choice2Button.setFont(choiceFont);
        choice3Button.setFont(choiceFont);
        choice4Button.setFont(choiceFont);



        combatItems = new ArrayList<>();
        combatWeapons = new ArrayList<>();








    }


    private void configureWindow() {
        setTitle(GAME_TITTLE);
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public void setStoryText(String text){

       typeStory(text,null);

       // storyTextArea.setText(text);
    }

    public void setChoices(List<Choice> choices){
        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button

        };

        updateBreakthroughButton();

        currentChoiceCount = choices.size();

        for(int i = 0; i < buttons.length; i++){

            if(i < choices.size()){
                buttons[i].setText(choices.get(i).getText());
                buttons[i].setVisible(false);
            }else{
                buttons[i].setVisible(false);
            }
        }
    }

    private void setCombatActions(List<CombatAction> actions) {

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        skillsButton.setVisible(false);

        for (int i = 0; i < buttons.length; i++) {

            if (i < actions.size()) {
                buttons[i].setText(actions.get(i).getName());
                buttons[i].setVisible(true);
            } else {
                buttons[i].setVisible(false);
            }
        }
    }


    public void showCombatSKills(){

        combatSkills.clear();

        combatSkills.addAll(player.getLearnedSkills());

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        int buttonIndex = 0;

        for(Skill skill : combatSkills){

            if(buttonIndex >= buttons.length){
                break;
            }

            buttons[buttonIndex].setText(skill.getName());
            buttons[buttonIndex].setVisible(true);

            buttonIndex++;
        }



        while(buttonIndex < buttons.length){

            buttons[buttonIndex].setVisible(false);
            buttonIndex++;
        }

        selectingCombatSkill = true;
       combatBackButton.setVisible(true);
    }



    private void updateScene() {

        StoryScene scene = storyManager.getCurrentScene();

        skillsButton.setVisible(false);

        setChoices(scene.getChoices());

        typeStory(scene.getStoryText(), () ->{
            showStoryChoices();
            skillsButton.setVisible(true);
        });
        //setStoryText(scene.getStoryText());

       // showStoryChoices();
       // skillsButton.setVisible(true);


    }



    private void updatePlayerInfo(){
        nameLabel.setText(("Name : " + player.getName()));
        realmLabel.setText("Realm : "+ player.getRealmName());
        hpLabel.setText("HP : "+ player.getHealthText());
        qiLabel.setText("Qi : " + player.getQiText());
        expLabel.setText("EXp : " + player.getExpText());
        defenseLabel.setText("Defense : " + player.getDefense());
        goldLabel.setText("Gold : " + player.getGold());
    }

    private void updateInventory(){

        inventoryTextArea.setText(
                        player.getInventory().getItemsText()
        );
    }


    private void handleBreakthrough(){

        if(player.breakthrough()){

            setStoryText("You successfully broke through!");

            updatePlayerInfo();
            updateInventory();
        }

        breakthroughButton.setVisible(false);

    }

    private void updateBreakthroughButton(){
        breakthroughButton.setVisible(player.canBreakthrough());
    }

    private void handleSkillScroll(int buttonIndex){

        if(buttonIndex == skillScrolls.size()){

            selectingSkillScroll = false;

            skillsButton.setVisible(true);

            updateScene();

            return;
        }

        if(buttonIndex >= skillScrolls.size()){
            return;
        }

        Item scroll = skillScrolls.get(buttonIndex);

        Skill skill = scroll.getSkill();

        if(skill == null){
            return;
        }


        boolean learned = player.learnSkillFromScroll(scroll);

        if(learned){

            setStoryText(
                    "you learned " + skill.getName()
            );

            updateInventory();
            updatePlayerInfo();
            showSkillScrolls();

        }
        else{
            setStoryText(
                    "You cannot learn " + skill.getName() + "."
            );

            selectingSkillScroll = false;;

            updateScene();
        }
    }

    private void handleCombatContinue(){

        combatContinueButton.setVisible(false);
        combatResult = false;

        if(combatManager.getEnemy().isAlive()){

            storyManager.startStory(1);
        } else{

            int nextSceneId = combatChoice.getNextSceneId();
            storyManager.completeCombat(nextSceneId);
        }

      //  skillsButton.setVisible(true);

        updateScene();
        updatePlayerInfo();
    }


    // buttons for combat
    private void handleCombatAction(int actionIndex){

        if(selectingCombatItem){

            useCombatItem(actionIndex);
            return;
        }

        if(selectingCombatAttack){

           useCombatWeapon(actionIndex);
            return;
        }
        if(actionIndex == 0) {

            selectingCombatAttack = true;

            showCombatWeapons();
            return;

        }

        else if(actionIndex == 1){

            selectingCombatItem = true;

            showCombatItems();

            return;


        }

        if(actionIndex == 2){

            selectingCombatSkill = true;

            showCombatSKills();

            combatBackButton.setVisible(true);

            return;
        }

        if (actionIndex == 3) {

            setStoryText("You try to escape.");

            return;
        }


    }


    private void handleCombatSkill(int skillIndex){

        if(skillIndex >= combatSkills.size()){
            return;
        }

        Skill skill = combatSkills.get(skillIndex);

        String result = combatManager.playerUseSkill(skill);

        setStoryText(result);

        updatePlayerInfo();

        if(combatManager.isCombatOver()){

            selectingCombatSkill = false;

            showCombatResult();

            return;
        }

        selectingCombatSkill = false;

        setCombatActions(combatManager.getActions());
    }



    private void showCombatResult(){
        inCombat = false;

        choice1Button.setVisible(false);
        choice2Button.setVisible(false);
        choice3Button.setVisible(false);
        choice4Button.setVisible(false);

        combatBackButton.setVisible(false);

        if (combatManager.getEnemy().isAlive()) {

            setStoryText(
                    combatManager.getEnemy().getName()
                            + " defeated you."
            );

        } else {

            setStoryText(
                    "You defeated "
                            + combatManager.getEnemy().getName()
                            + "!\n\n"
                            + "Exp gained: "
                            + combatManager.getEnemy().getExpReward()
            );
        }

        combatContinueButton.setVisible(true);

        updatePlayerInfo();
    }




    public void showCombatItems(){

        combatItems.clear();

        List<InventoryItem> items = player.getInventory().getItems();

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        int buttonIndex = 0;

        for(InventoryItem inventoryItem : items){

            Item item = inventoryItem.getItem();

            if(item.isConsumable() && item.getType() != ItemType.SKILL){

                if(buttonIndex >= buttons.length){
                    break;
                }

                combatItems.add(item);

                buttons[buttonIndex].setText(
                        item.getName() + " X" + inventoryItem.getQuantity()
                );
                buttons[buttonIndex].setVisible(true);

                buttonIndex++;
            }
        }

        while(buttonIndex < buttons.length){
            buttons[buttonIndex].setVisible(false);
            buttonIndex++;
        }

        combatBackButton.setVisible(true);
    }

    private void showCombatWeapons() {

        combatWeapons.clear();

        List<InventoryItem> items =
                player.getInventory().getItems();

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        int buttonIndex = 0;

        buttons[buttonIndex].setText("Fist");
        buttons[buttonIndex].setVisible(true);

        combatWeapons.add(null);

        buttonIndex++;


        // adding weapon from item:
        for (InventoryItem inventoryItem : items) {

            Item item = inventoryItem.getItem();

            if (item.getType() == ItemType.WEAPON) {

                if (buttonIndex >= buttons.length) {
                    break;
                }

                combatWeapons.add(item);

                buttons[buttonIndex].setText(item.getName());
                buttons[buttonIndex].setVisible(true);

                buttonIndex++;
            }
        }



        while (buttonIndex < buttons.length) {
            buttons[buttonIndex].setVisible(false);
            buttonIndex++;
        }

        combatBackButton.setVisible(true);
    }

    private void useCombatWeapon(int weaponIndex){

        if(weaponIndex >= combatWeapons.size()){
            return;
        }

        Item weapon  = combatWeapons.get(weaponIndex);

        String result;
        if(weapon == null){
            result = combatManager.playerAttack();
        }else{
            result = combatManager.playerAttack(weapon);
        }

        setStoryText(result);

        updatePlayerInfo();

        if(combatManager.isCombatOver()){
            showCombatResult();

            return;
        }

        selectingCombatAttack = false;
        setCombatActions(combatManager.getActions());

    }



    private void useCombatItem(int itemIndex){

        if(itemIndex >= combatItems.size()){
            return;
        }

        Item item = combatItems.get(itemIndex);

        String result = combatManager.playerUseItem(item);

        if(result == null){
            return;
        }

        setStoryText(result);

        updatePlayerInfo();
        updateInventory();

        if(combatManager.isCombatOver()){

            inCombat = false;
            selectingCombatItem = false;

            int nextSceneId = combatChoice.getNextSceneId();

            storyManager.completeCombat(nextSceneId);

            updateScene();
            updatePlayerInfo();

            return;
        }

        selectingCombatItem = false;

        setCombatActions(combatManager.getActions());


    }


    private void showMainCombatActions(){

        selectingCombatItem = false;
        selectingCombatAttack =  false;
        selectingCombatSkill = false;

        combatBackButton.setVisible(false);

        setCombatActions(combatManager.getActions());
    }



    private void handleChoice(int choiceIndex){

        StoryScene scene = storyManager.getCurrentScene();
        Choice choice = scene.getChoices().get(choiceIndex);

        if(choice.getType() == ChoiceType.COMBAT){
            enterCombat(choice);
            return;
        } else if(choice.getType() == ChoiceType.NPC){
            enterNPC(choice);
            return;
        } else if(choice.getType() == ChoiceType.NPC_BACK){

            showNPCMenu();
            return;
        }

       boolean success =  storyManager.selectChoice(choiceIndex);
        if(success) {
            updateScene();
            updateInventory();
            updatePlayerInfo();
        }else{
            setStoryText(storyManager.getLastMessage());
        }
    }
    // temp
    private void enterCombat(Choice choice) {

        inCombat = true;



        skillsButton.setVisible(false);

        selectingSkillScroll = false;
        selectingCombatSkill = false;

        skillsButton.setVisible(false);

        combatChoice = choice;

        Enemy enemy = new Enemy(choice.getNpc());

        combatManager = new CombatManager(player,enemy);

        combatManager.startCombat();

        showCombatStart();

        setCombatActions(combatManager.getActions());

    }

    private void showCombatStart() {

        Enemy enemy = combatManager.getEnemy();

        setStoryText(
                enemy.getName() + " appears!\n\n" +
                        enemy.getName() + " HP: " +
                        enemy.getHp() + "/" + enemy.getMaxHp() + "\n\n" +
                        player.getName() + " HP: " +
                        player.getHealthText()
        );
    }


    private void showSkillScrolls(){

        selectingSkillScroll = true;

        skillsButton.setVisible(false);

        combatBackButton.setVisible(false);

        skillScrolls.clear();

        for(InventoryItem inventoryItem : player.getInventory().getItems()){

            Item item = inventoryItem.getItem();

            if(item.getType() == ItemType.SKILL){
                skillScrolls.add(item);
            }
        }

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        int buttonIndex = 0;

        for(Item scroll : skillScrolls){

            if(buttonIndex >= buttons.length -1){
                break;
            }

            buttons[buttonIndex].setText(scroll.getName());
            buttons[buttonIndex].setVisible(true);

            buttonIndex++;
        }

       buttons[buttonIndex].setText("Back");
       buttons[buttonIndex].setVisible(true);

        buttonIndex++;

        // Hide remaining buttons
        while (buttonIndex < buttons.length) {

            buttons[buttonIndex].setVisible(false);
            buttonIndex++;
        }
    }


    private void enterNPC(Choice choice){

        inNPC = true;
        skillsButton.setVisible(false);

        npcChoice = choice;

        currentNPC = choice.getNpc();
        showNPCMenu();
    }

    private void showNPCMenu(){

        inNPC = true;

        viewingNPCProfile = false;
        selectingTradeItem = false;
        selectingSkillScroll = false;

        skillsButton.setVisible(false);
        combatBackButton.setVisible(true);

        NPC npc = currentNPC;

        setStoryText(npc.getProfile().getName() + "\n\nwhat do you want to do?");

        choice1Button.setText("Talk");
        choice1Button.setVisible(true);

        choice2Button.setText("Profile");
        choice2Button.setVisible(true);





        if(npc.canTrade()) {
            choice3Button.setText("Trade");
            choice3Button.setVisible(true);

        }else{
            choice3Button.setVisible(false);
        }


        if(npc.canFight()){
            choice4Button.setText("Fight");
            choice4Button.setVisible(true);
        }else{
            choice4Button.setVisible(false);
        }

        combatBackButton.setVisible(true);
    }



    private void handleNPCAction(int actionIndex) {

        if (actionIndex == 0) {

            // Talk
            handleNPCTalk();

        } else if (actionIndex == 1) {

            // Profile
            showNPCProfile();

        } else if (actionIndex == 2) {

            // Trade
           // handleNPCTrade();
            showNPCTrade();

        } else if (actionIndex == 3) {

            // Fight
            if (currentNPC.canFight()) {
                enterNPCCombat();
            }
        }
    }

    private void leaveNPC() {

        inNPC = false;
        currentNPC = null;

        combatBackButton.setVisible(false);

        storyManager.startStory(npcChoice.getNextSceneId());

        npcChoice = null;

        updateScene();
        updatePlayerInfo();

    }


    private void handleNPCTalk(){

        int talkSceneId = currentNPC.getProfile().getTalkSceneId();

        inNPC = false;

        combatBackButton.setVisible(false);
        selectingSkillScroll = false;

        storyManager.startStory(talkSceneId);

        updateScene();
        updatePlayerInfo();
    }

    private void enterNPCCombat(){

        inNPC = false;
        inCombat = true;
        skillsButton.setVisible(false);

        combatChoice = npcChoice;

        Enemy enemy = new Enemy(currentNPC);

        combatManager = new CombatManager(player, enemy);

        combatManager.startCombat();
        showCombatStart();

        setCombatActions(combatManager.getActions());
    }


    private void showNPCProfile(){
        NPCProfile profile = currentNPC.getProfile();

        setStoryText(
                "Name: " + profile.getName() +
                "\nTitle: " + profile.getTitle() +
                "\nRealm: " + profile.getRealm() +
                "\nPersonality: " + profile.getPersonality() +
                "\n\n" + profile.getDescription()
        );

        choice1Button.setVisible(false);
        choice2Button.setVisible(false);
        choice3Button.setVisible(false);
        choice4Button.setVisible(false);

        combatBackButton.setVisible(true);
        skillsButton.setVisible(false);
        viewingNPCProfile = true;
    }


    private void showNPCTrade(){

        selectingTradeItem = true;
        combatBackButton.setVisible(false);

        List<InventoryItem> tradeItems = currentNPC.getTradeItems();

        setStoryText(
                currentNPC.getProfile().getName() +
                        "\n\nWhat do you want to buy?"
        );


        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        for(JButton button : buttons){
            button.setVisible(false);
        }

        int buttonIndex = 0;

        for(InventoryItem inventoryItem : tradeItems){

            if(buttonIndex >= buttons.length -1){
                break;
            }

            buttons[buttonIndex].setText(
                    inventoryItem.getItem().getName()
                    + " x "
                    + inventoryItem.getQuantity()
                    + " - "
                    + inventoryItem.getItem().getPrice()
                    + " gold"
            );
            buttons[buttonIndex].setVisible(true);


            buttonIndex++;
        }
        if(buttonIndex < buttons.length){
            buttons[buttonIndex].setText("Back");
            buttons[buttonIndex].setVisible(true);
        }
    }

    // form gamewindow
    private boolean buyItem(Item item){

        int price = item.getPrice();

        if(!player.spendGold(price)){

            setStoryText(
                    "You don't have enough gold.\n\n" +
                    item.getName() +
                    " costs " + price +
                    " Gold.\n" +
                    " You have " + player.getGold() + " Gold"
            );
            return false;
        }

        if(!currentNPC.buyItem(item)){

            setStoryText("This item is out of stock.");

            player.addGold(price);

            return false;
        }


        player.addItem(item);

        setStoryText(
                "You Bought " + item.getName() +
                " for " + price + " Gold."
        );
        updatePlayerInfo();
        updateInventory();


        return true;
    }


    private void handleNPCTrade(int itemIndex){

        List<InventoryItem> tradeItems = currentNPC.getTradeItems();

        if(itemIndex >= tradeItems.size()){

            selectingTradeItem = false;
            showNPCMenu();
            return;
        }

        InventoryItem inventoryItem = tradeItems.get(itemIndex);

        buyItem((inventoryItem.getItem()));


    }



    private void typeStory(String text, Runnable onFinished){


        if(storyTimer != null && storyTimer.isRunning()){
            storyTimer.stop();
        }

        storyTextArea.setText("");

        final int[] index = {0};

        storyTimer = new Timer (20,e ->{

            if(index[0] < text.length()){

                storyTextArea.append(
                        String.valueOf(text.charAt(index[0]))
                );

                index[0]++;
            } else {
                storyTimer.stop();

                if(onFinished != null){
                    onFinished.run();
                }
            }

        });
        storyTimer.start();
    }


    private void showStoryChoices() {

        JButton[] buttons = {
                choice1Button,
                choice2Button,
                choice3Button,
                choice4Button
        };

        for (int i = 0; i < currentChoiceCount; i++) {
            buttons[i].setVisible(true);
        }



        choicePanel.revalidate();
        choicePanel.repaint();
    }




    public void showWindow(){
        setVisible(true);
    }

}
