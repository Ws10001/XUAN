package GUI1;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class AgentConfigurationGUI extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private CardLayout cardLayout;
    private Map<String, JPanel> cardMap = new HashMap<>();

    public AgentConfigurationGUI() {
        setTitle("智能体配置系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        Container container = getContentPane();//获得画布
        container.setLayout(new BorderLayout(10, 10));

        createLeftPanel();
        createRightPanel();

        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.CENTER);
        ((JPanel)container).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void createLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, 800));
        leftPanel.setBorder(BorderFactory.createTitledBorder("导航"));

        ButtonGroup buttonGroup = new ButtonGroup();//按钮组，同一时间只能选中一个按钮

        // 顶部导航
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        String[] topButtons = {"文件","帮助"};
        for (String name : topButtons) {
            addNavigationButton(topPanel, buttonGroup, name);
        }

        // 功能导航
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBorder(BorderFactory.createTitledBorder("功能"));

        String[] functionButtons = {
                 "行为组合器", "自定义行为", "配置管理","智能体注册"
        };
        for (String name : functionButtons) {
            addNavigationButton(middlePanel, buttonGroup, name);
        }

        leftPanel.add(topPanel);
        leftPanel.add(middlePanel);
        leftPanel.add(Box.createVerticalGlue());//按钮向上集中
    }
    private void addNavigationButton(JPanel panel, ButtonGroup buttonGroup, String name) {
        JToggleButton button = new JToggleButton(name);//开关按钮，选中保持高亮
        button.setAlignmentX(Component.CENTER_ALIGNMENT);//按钮水平居中
        button.setMaximumSize(new Dimension(180, 40));
        button.setPreferredSize(new Dimension(180, 40));
        button.addActionListener(e -> cardLayout.show(rightPanel, name));
        buttonGroup.add(button);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void createRightPanel() {
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.setBorder(BorderFactory.createTitledBorder("内容区域"));
        // 创建各个卡片
        createSimpleCard("文件", "文件管理功能区域");
        createSimpleCard("帮助", "帮助文档区域");
        HelpPanel helpPanel = new HelpPanel();
        rightPanel.add(helpPanel,"帮助");
        cardMap.put("帮助", helpPanel);
        // 集成行为组合器面板
        AgentDesignerPanel agentDesignerPanel = new AgentDesignerPanel();
        rightPanel.add(agentDesignerPanel, "行为组合器");
        cardMap.put("行为组合器", agentDesignerPanel);
        // 集成自定义行为面板
        CustomBehaviorPanel customBehaviorPanel = new CustomBehaviorPanel();
        rightPanel.add(customBehaviorPanel, "自定义行为");
        cardMap.put("自定义行为", customBehaviorPanel);
        // 集成智能体配置面板
        AgentConfigPanel configPanel = new AgentConfigPanel();
        rightPanel.add(configPanel, "配置管理");
        cardMap.put("配置管理", configPanel);
        // 集成智能体注册面板
        RegisterAgent registerAgent = new RegisterAgent();
        rightPanel.add(registerAgent,"智能体注册");
        cardMap.put("智能体注册", registerAgent);
        cardLayout.show(rightPanel, "文件");
    }

    private void createSimpleCard(String cardName, String defaultText) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel(cardName + " 配置", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        cardPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea(defaultText + "\n\n您可以在这里添加具体的配置内容。");
        contentArea.setEditable(false);//不可编辑
        contentArea.setFont(new Font("宋体", Font.PLAIN, 14));
        contentArea.setLineWrap(true);//自动换行
        contentArea.setWrapStyleWord(true);//按文字换行

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cardPanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(cardPanel, cardName);
        cardMap.put(cardName, cardPanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // 设置外观为系统默认外观
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> { // 保证线程安全
            AgentConfigurationGUI gui = new AgentConfigurationGUI();
            gui.setVisible(true); // 设置界面为可见
        });
    }
}