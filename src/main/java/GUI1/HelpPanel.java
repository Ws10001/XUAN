package GUI1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 工业多智能体系统 - 详细帮助面板
 */
public class HelpPanel extends JPanel {
    // 右侧核心内容展示区
    private final JTextArea contentShowArea;

    // 全局样式配置
    private static final Font BTN_FONT      = new Font("微软雅黑", Font.PLAIN, 14);
    private static final Font CONTENT_FONT  = new Font("微软雅黑", Font.PLAIN, 14);
    private static final Font TITLE_FONT    = new Font("微软雅黑", Font.BOLD, 20);
    private static final int    BTN_WIDTH    = 340;

    public HelpPanel() {
        // 主面板初始化
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //顶部标题
        JLabel title = new JLabel(" 工业多智能体系统帮助中心", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        add(title, BorderLayout.NORTH);

        //功能按钮区
        JPanel btnGroup = new JPanel();
        btnGroup.setLayout(new GridLayout(5, 1, 10, 10));
        btnGroup.setPreferredSize(new Dimension(BTN_WIDTH, 0));

        // 创建5个核心切换按钮

        JButton btn1 = createContentBtn("1. 智能体设计面板",        getAgentDesignerPanelFullHelp());
        JButton btn2 = createContentBtn("2. 自定义行为面板",        getCustomBehaviorPanelFullHelp());
        JButton btn3 = createContentBtn("3. 智能体参数配置面板",  getAgentConfigPanelFullHelp());
        JButton btn4 = createContentBtn("4. 智能体注册面板",        getRegisterAgentPanelFullHelp());
        JButton btn5 = createContentBtn("5. 新手指导",     getNewbieFullGuide());

        btnGroup.add(btn1);
        btnGroup.add(btn2);
        btnGroup.add(btn3);
        btnGroup.add(btn4);
        btnGroup.add(btn5);

        // 详细内容展示区
        contentShowArea = new JTextArea();
        contentShowArea.setFont(CONTENT_FONT);
        contentShowArea.setLineWrap(true);// 开启自动换行
        contentShowArea.setWrapStyleWord(true);// 按完整单词换行
        contentShowArea.setEditable(false);
        contentShowArea.setMargin(new Insets(15, 15, 15, 15));
        // 滚动条
        JScrollPane scrollPane = new JScrollPane(contentShowArea);
        //左右分割布局
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, btnGroup, scrollPane);  // 创建水平分割面板，左侧按钮组、右侧滚动面板
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);
    }
    /**
     * 创建按钮
     */
    private JButton createContentBtn(String text, String content) {
        JButton btn = new JButton(text);
        btn.setFont(BTN_FONT);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            contentShowArea.setText(content);
            contentShowArea.setCaretPosition(0); // 自动回到顶部
        });
        return btn;
    }
    /**
     * 1. 智能体参数配置面板
     */
    private String getAgentConfigPanelFullHelp() {
        return "【3. 智能体参数配置面板】\n"
                + "========================================================\n"
                + "【一、面板定位】\n"
                + "本面板是整个多智能体系统的【数据源核心】，所有智能体的参数全部在此配置，\n"
                + "自动生成 ConfigData.java 配置文件\n"
                + "【二、界面全区域拆解】\n"
                + "1. 智能体基础信息区\n"
                + "    智能体名称输入框：必填，全局唯一\n"
                + "    类型下拉框：6种工业智能体（Base/AGV/机械臂Arm/缓冲站Buffer/机床Machine/仓库Warehouse）\n"
                + "2. JSON参数编辑区\n"
                + "    多行输入框：填写标准JSON参数，系统自动格式化、补全符号、兼容不规范输入\n"
                + "3. 功能按钮区\n"
                + "    保存配置：生成/覆盖配置方法，写入ConfigData.java\n"
                + "    刷新列表：重新读取配置文件，同步最新智能体列表\n"
                + "    查看配置：弹窗显示完整配置代码\n"
                + "    删除配置：永久删除智能体配置\n"
                + "4. 配置列表区\n"
                + "    展示所有已配置智能体名称、类型、配置状态\n"
                + "    底部显示配置总数\n"
                + "【三、6类智能体专属参数说明】\n"
                + " Base：基础通用智能体\n"
                + " AGV：自动导引车\n"
                + " Arm：机械臂\n"
                + " Buffer：缓冲站\n"
                + " Machine：机床\n"
                + " Warehouse：仓库\n\n"
                + "【四、标准操作流程】\n"
                + "步骤1：输入唯一智能体名称\n"
                + "步骤2：选择对应智能体类型\n"
                + "步骤3：填写合法JSON参数\n"
                + "步骤4：点击【保存配置】\n"
                + "步骤5：在列表中确认配置生成成功\n"
                + "【五、JSON格式强制规范】\n"
                + "正确示例：{\"ip\":\"192.168.1.100\",\"port\":8080,\"maxSpeed\":1.5}\n"
                + "禁止：中文符号、单引号、多余逗号、未闭合括号、重复Key\n"
                + "【六、异常处理】\n"
                + "1. 保存失败 → JSON格式错误\n"
                + "2. 列表不更新 → 点击【刷新列表】\n"
                + "3. 配置丢失 → 名称重复被覆盖\n"
                + "【七、绝对禁止行为】\n"
                + "1. 禁止重复命名智能体\n"
                + "2. 禁止使用中文、特殊符号作为名称";
    }
    /**
     * 2. 智能体设计面板
     */
    private String getAgentDesignerPanelFullHelp() {
        return "【1. 智能体设计面板】\n"
                + "========================================================\n"
                + "【一、面板定位】\n"
                + "所有智能体上线的第一步，定义智能体身份、所属分区，\n"
                + "设计结果自动同步到参数配置面板，无设计则无法配置参数。\n"
                + "【二、界面全区域拆解】\n"
                + "1. 身份定义区\n"
                + "    唯一ID：系统全局识别码\n"
                + "    显示别名：界面可视化名称\n"
                + "2. 状态标志位（FlagBit）定义区\n"
                + "    编码：数字\n"
                + "    名称：状态描述\n"
                + "3. 操作按钮\n"
                + "    新建智能体：清空表单，开始新建设计\n"
                + "    保存设计：存入系统缓存，同步至参数面板\n"
                + "    加载设计：读取已保存的智能体设计\n"
                + "    导出设计：备份设计方案\n"
                + "4. 实时预览区：展示完整设计信息\n"
                + "【三、核心规则】\n"
                + "1. 唯一ID全系统不可重复\n"
                + "2. 标志位编码必须从0开始连续数字\n"
                + "3. 集群分区必须与系统预设一致\n"
                + "4. 设计完成后才能进入参数配置\n"
                + "【四、异常处理】\n"
                + "1. ID重复 → 修改ID格式\n"
                + "2. 标志位无效 → 修改\n"
                + "3. 设计不同步 → 重新保存设计\n";
    }
    /**
     * 3. 自定义行为面板
     */
    private String getCustomBehaviorPanelFullHelp() {
        return "【3. 自定义行为面板｜业务逻辑配置器｜超级详细说明】\n"
                + "========================================================\n"
                + "【一、面板定位】\n"
                + "可视化配置智能体动作逻辑，无需编写代码\n"
                + "决定智能体做什么\n"
                + "【二、界面全区域拆解】\n"
                + "1. 智能体匹配区\n"
                + "    选择已设计的智能体\n"
                + "    自动显示智能体类型\n"
                + "2. 行为选择库\n"
                + "    基础行为\n"
                + "    专属行为\n"
                + "【三、标准流程】\n"
                + "选择智能体→加载行为库→选择行为→配置参数→预览→保存同步\n"
                + "【四、异常处理】\n"
                + "1. 行为不显示 → 未完成智能体设计\n"
                + "2. 不同步 → 刷新参数配置面板\n"
                + "【六、禁止行为】\n"
                + "1. 禁止跨类型配置行为\n";
    }
    /**
     * 4. 智能体注册面板
     */
    private String getRegisterAgentPanelFullHelp() {
        return "【4. 智能体注册面板｜系统上线激活器｜超级详细说明】\n"
                + "========================================================\n"
                + "【一、面板定位】\n"
                + "智能体上线，将配置完成的智能体注册到JADE系统容器，激活运行、参与任务调度，\n"
                + "未注册智能体永远处于离线状态。\n"
                + "【二、界面全区域拆解】\n"
                + "1. 配置加载区\n"
                + "    刷新配置：读取ConfigData.java所有智能体\n"
                + "    列表展示：名称、类型、注册状态\n"
                + "2. 注册操作区\n"
                + "    注销：停止运行，从系统移除\n"
                + "    重启：重置运行状态\n"
                + "【三、标准流程】\n"
                + "注册→查看运行状态\n"
                + "【四、异常处理】\n"
                + "1. 注册失败 → 容器未启动/配置错误/ID重复\n"
                + "2. 列表为空 → 未保存参数配置\n"
                + "【五、禁止行为】\n"
                + "1. 容器离线禁止注册\n"
                + "2. 禁止删除运行中智能体的配置\n"
                + "3. 生产环境禁止注销正在执行任务的智能体";
    }
    /**
     * 5. 新手全套指导
     */
    private String getNewbieFullGuide() {
        return "【新手全套指导｜工业多智能体系统】\n"
                + "========================================================\n"
                + "【前置说明】\n"
                + " 必须按顺序操作：设计 → 行为 → 参数 → 注册\n"
                + "第一步：智能体设计\n"
                + "1. 打开【智能体设计面板】\n"
                + "2. 点击【创建新智能体】\n"
                + "3. 唯一ID：AGV01（前缀）\n"
                + "4. 别名：AGV小车1号\n"
                + "5. 进行行为配置         "
                + "6. 点击【保存设计】\n"
                + " 完成：智能体身份创建成功\n"
                + "第二步：自定义行为\n"
                + "1. 打开【自定义行为面板】\n"
                + "2. 创建新行为\n"
                + "3. 点击【预览行为】\n"
                + "4. 点击【保存行为】\n"
                + " 完成：行为同步到参数\n"
                + "第三步：参数配置\n"
                + "1. 打开【参数配置面板】\n"
                + "2. 名称：AGV01\n"
                + "3. 类型：AGV\n"
                + "4. JSON：{\"ip\":\"192.168.1.100\",\"port\":8080}\n"
                + "5. 点击【保存配置】\n"
                + " 完成：配置文件生成\n"
                + "第四步：注册上线\n"
                + "1. 打开【注册面板】\n"
                + "2. 确认主容器在线\n"
                + "3. 点击【刷新配置】\n"
                + "4. 勾选AGV01\n"
                + "5. 点击【注册】\n"
                + "6. 状态=运行中\n"
                + " 完成：智能体正式上线\n"
                + "高频问题\n"
                + "1. 保存失败 → JSON格式错误\n"
                + "2. 注册失败 → 主容器未启动\n"
                + "3. 行为不显示 → 未完成设计\n"
                + "4. ID重复 → 修改编号\n"
                + "5. 参数不同步 → 刷新面板\n"
                + "6. 列表为空 → 未保存配置\n"
                + "7. 运行异常 → 注销重注册\n"
                + "8. 界面卡顿 → 重启GUI\n"
                + "9. 标志位无效 → 使用数字\n"
                + "10. 行为报错 → 检查重新设计\n";
    }
    // 测试入口：直接运行查看完整效果
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("超级详细帮助中心 - 左右分栏版");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null);
            frame.add(new HelpPanel());
            frame.setVisible(true);
        });
    }
}
