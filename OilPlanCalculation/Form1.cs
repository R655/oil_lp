using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using GlpkSharp;

namespace OilPlanCalculation
{
    public partial class Form1 : Form
    {
        OilLP oilLp;
        Templater templater;
 
        public Form1()
        {
            InitializeComponent();
            templater = new Templater();
            templater.loadTemplate(@"Templater\OutputTemplate.cshtml");

            подключитьсяToolStripMenuItem_Click(null, null);
            //findSolution_Click(null, null);
            //showSolutionDetails_Click(null, null);
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void подключитьсяToolStripMenuItem_Click(object sender, EventArgs e)
        {
            oilLp = new OilLP(new ForDB.DataManager("data.sqlite"));
            подключитьсяToolStripMenuItem.Enabled = false;
            findSolution.Enabled = true;
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private string formatFloatValue(double value)
        {
            String FloatFormat = "{0:G4}";
            double Zero = 1.0E-10;

            Double dvalue = (Double)value;
            if (Math.Abs(dvalue) < Zero)
                dvalue = 0;
            return String.Format(FloatFormat, (Double)dvalue);
        }

        private void findSolution_Click(object sender, EventArgs e)
        {

            if (oilLp.findSolution())
            {
                int i = 0;
                foreach (var item in oilLp.output.tools)
                {
                    dataGridView1.Rows.Add();
                    dataGridView1.Rows[i].Cells[0].Value = item.name;
                    dataGridView1.Rows[i].Cells[1].Value = item.maxPower;
                    dataGridView1.Rows[i].Cells[2].Value = formatFloatValue(item.recPower);
                    ++i;
                }
            }

        }

        private void showSolutionDetails_Click(object sender, EventArgs e)
        {
            templater.renderTemplate(oilLp.output);
        }
    }
}
