namespace OilPlanCalculation
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.подключитьсяToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.справочникиToolStripMenuItem1 = new System.Windows.Forms.ToolStripMenuItem();
            this.продукцияToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.ресурсToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.установкаToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.рабочиеТаблицыToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.требуемыеРесурсыДляУстановкиToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.требуемаяПромежуточнаяПродукцияДляУстановкиToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.получаемаяПродукцияToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.запасыРесурсовToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.label1 = new System.Windows.Forms.Label();
            this.numericUpDown1 = new System.Windows.Forms.NumericUpDown();
            this.findSolution = new System.Windows.Forms.Button();
            this.dataGridView1 = new System.Windows.Forms.DataGridView();
            this.ToolName = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.ToolMaxPower = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.ToolRecPower = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.showSolutionDetails = new System.Windows.Forms.Button();
            this.menuStrip1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numericUpDown1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            this.SuspendLayout();
            // 
            // menuStrip1
            // 
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.exitToolStripMenuItem,
            this.подключитьсяToolStripMenuItem,
            this.справочникиToolStripMenuItem1,
            this.рабочиеТаблицыToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Size = new System.Drawing.Size(515, 24);
            this.menuStrip1.TabIndex = 0;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // exitToolStripMenuItem
            // 
            this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
            this.exitToolStripMenuItem.Size = new System.Drawing.Size(52, 20);
            this.exitToolStripMenuItem.Text = "Выход";
            this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
            // 
            // подключитьсяToolStripMenuItem
            // 
            this.подключитьсяToolStripMenuItem.Name = "подключитьсяToolStripMenuItem";
            this.подключитьсяToolStripMenuItem.Size = new System.Drawing.Size(95, 20);
            this.подключитьсяToolStripMenuItem.Text = "Подключиться";
            this.подключитьсяToolStripMenuItem.Click += new System.EventHandler(this.подключитьсяToolStripMenuItem_Click);
            // 
            // справочникиToolStripMenuItem1
            // 
            this.справочникиToolStripMenuItem1.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.продукцияToolStripMenuItem,
            this.ресурсToolStripMenuItem,
            this.установкаToolStripMenuItem});
            this.справочникиToolStripMenuItem1.Name = "справочникиToolStripMenuItem1";
            this.справочникиToolStripMenuItem1.Size = new System.Drawing.Size(86, 20);
            this.справочникиToolStripMenuItem1.Text = "Справочники";
            // 
            // продукцияToolStripMenuItem
            // 
            this.продукцияToolStripMenuItem.Name = "продукцияToolStripMenuItem";
            this.продукцияToolStripMenuItem.Size = new System.Drawing.Size(130, 22);
            this.продукцияToolStripMenuItem.Text = "Продукция";
            // 
            // ресурсToolStripMenuItem
            // 
            this.ресурсToolStripMenuItem.Name = "ресурсToolStripMenuItem";
            this.ресурсToolStripMenuItem.Size = new System.Drawing.Size(130, 22);
            this.ресурсToolStripMenuItem.Text = "Ресурс";
            // 
            // установкаToolStripMenuItem
            // 
            this.установкаToolStripMenuItem.Name = "установкаToolStripMenuItem";
            this.установкаToolStripMenuItem.Size = new System.Drawing.Size(130, 22);
            this.установкаToolStripMenuItem.Text = "Установка";
            // 
            // рабочиеТаблицыToolStripMenuItem
            // 
            this.рабочиеТаблицыToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.требуемыеРесурсыДляУстановкиToolStripMenuItem,
            this.требуемаяПромежуточнаяПродукцияДляУстановкиToolStripMenuItem,
            this.получаемаяПродукцияToolStripMenuItem,
            this.запасыРесурсовToolStripMenuItem});
            this.рабочиеТаблицыToolStripMenuItem.Name = "рабочиеТаблицыToolStripMenuItem";
            this.рабочиеТаблицыToolStripMenuItem.Size = new System.Drawing.Size(108, 20);
            this.рабочиеТаблицыToolStripMenuItem.Text = "Рабочие таблицы";
            // 
            // требуемыеРесурсыДляУстановкиToolStripMenuItem
            // 
            this.требуемыеРесурсыДляУстановкиToolStripMenuItem.Name = "требуемыеРесурсыДляУстановкиToolStripMenuItem";
            this.требуемыеРесурсыДляУстановкиToolStripMenuItem.Size = new System.Drawing.Size(347, 22);
            this.требуемыеРесурсыДляУстановкиToolStripMenuItem.Text = "Требуемые ресурсы для установки";
            // 
            // требуемаяПромежуточнаяПродукцияДляУстановкиToolStripMenuItem
            // 
            this.требуемаяПромежуточнаяПродукцияДляУстановкиToolStripMenuItem.Name = "требуемаяПромежуточнаяПродукцияДляУстановкиToolStripMenuItem";
            this.требуемаяПромежуточнаяПродукцияДляУстановкиToolStripMenuItem.Size = new System.Drawing.Size(347, 22);
            this.требуемаяПромежуточнаяПродукцияДляУстановкиToolStripMenuItem.Text = "Требуемая промежуточная продукция для установки";
            // 
            // получаемаяПродукцияToolStripMenuItem
            // 
            this.получаемаяПродукцияToolStripMenuItem.Name = "получаемаяПродукцияToolStripMenuItem";
            this.получаемаяПродукцияToolStripMenuItem.Size = new System.Drawing.Size(347, 22);
            this.получаемаяПродукцияToolStripMenuItem.Text = "Получаемая продукция";
            // 
            // запасыРесурсовToolStripMenuItem
            // 
            this.запасыРесурсовToolStripMenuItem.Name = "запасыРесурсовToolStripMenuItem";
            this.запасыРесурсовToolStripMenuItem.Size = new System.Drawing.Size(347, 22);
            this.запасыРесурсовToolStripMenuItem.Text = "Запасы ресурсов";
            // 
            // label1
            // 
            this.label1.Location = new System.Drawing.Point(13, 28);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(306, 23);
            this.label1.TabIndex = 1;
            this.label1.Text = "Вероятность безошибочности данных о запасах ресурсов";
            this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // numericUpDown1
            // 
            this.numericUpDown1.DecimalPlaces = 2;
            this.numericUpDown1.Location = new System.Drawing.Point(326, 30);
            this.numericUpDown1.Name = "numericUpDown1";
            this.numericUpDown1.Size = new System.Drawing.Size(57, 20);
            this.numericUpDown1.TabIndex = 2;
            this.numericUpDown1.Value = new decimal(new int[] {
            90,
            0,
            0,
            0});
            // 
            // findSolution
            // 
            this.findSolution.Enabled = false;
            this.findSolution.Location = new System.Drawing.Point(389, 27);
            this.findSolution.Name = "findSolution";
            this.findSolution.Size = new System.Drawing.Size(110, 23);
            this.findSolution.TabIndex = 3;
            this.findSolution.Text = "Рассчитать";
            this.findSolution.UseVisualStyleBackColor = true;
            this.findSolution.Click += new System.EventHandler(this.findSolution_Click);
            // 
            // dataGridView1
            // 
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.ToolName,
            this.ToolMaxPower,
            this.ToolRecPower});
            this.dataGridView1.Location = new System.Drawing.Point(0, 56);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.Size = new System.Drawing.Size(515, 288);
            this.dataGridView1.TabIndex = 4;
            // 
            // ToolName
            // 
            this.ToolName.HeaderText = "Установка";
            this.ToolName.Name = "ToolName";
            this.ToolName.ReadOnly = true;
            // 
            // ToolMaxPower
            // 
            this.ToolMaxPower.HeaderText = "Максимальная мощность";
            this.ToolMaxPower.Name = "ToolMaxPower";
            this.ToolMaxPower.ReadOnly = true;
            // 
            // ToolRecPower
            // 
            this.ToolRecPower.HeaderText = "Рекомендуемая загрузка";
            this.ToolRecPower.Name = "ToolRecPower";
            this.ToolRecPower.ReadOnly = true;
            // 
            // showSolutionDetails
            // 
            this.showSolutionDetails.Enabled = false;
            this.showSolutionDetails.Location = new System.Drawing.Point(290, 350);
            this.showSolutionDetails.Name = "showSolutionDetails";
            this.showSolutionDetails.Size = new System.Drawing.Size(213, 23);
            this.showSolutionDetails.TabIndex = 5;
            this.showSolutionDetails.Text = "Получить дополнительные данные";
            this.showSolutionDetails.UseVisualStyleBackColor = true;
            this.showSolutionDetails.Click += new System.EventHandler(this.showSolutionDetails_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(515, 380);
            this.Controls.Add(this.showSolutionDetails);
            this.Controls.Add(this.dataGridView1);
            this.Controls.Add(this.findSolution);
            this.Controls.Add(this.numericUpDown1);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.menuStrip1);
            this.MainMenuStrip = this.menuStrip1;
            this.Name = "Form1";
            this.Text = "OilPlanCalculation";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numericUpDown1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem exitToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem справочникиToolStripMenuItem1;
        private System.Windows.Forms.ToolStripMenuItem продукцияToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem ресурсToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem установкаToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem рабочиеТаблицыToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem требуемыеРесурсыДляУстановкиToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem требуемаяПромежуточнаяПродукцияДляУстановкиToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem получаемаяПродукцияToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem запасыРесурсовToolStripMenuItem;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.NumericUpDown numericUpDown1;
        private System.Windows.Forms.Button findSolution;
        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.Button showSolutionDetails;
        private System.Windows.Forms.ToolStripMenuItem подключитьсяToolStripMenuItem;
        private System.Windows.Forms.DataGridViewTextBoxColumn ToolName;
        private System.Windows.Forms.DataGridViewTextBoxColumn ToolMaxPower;
        private System.Windows.Forms.DataGridViewTextBoxColumn ToolRecPower;
    }
}

