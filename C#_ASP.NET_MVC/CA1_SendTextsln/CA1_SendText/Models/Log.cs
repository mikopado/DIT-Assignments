using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CA1_SendText.Models
{
    public static class Log
    {
        public static void Message(string name, string text)
        {
            System.Diagnostics.Debug.WriteLine("{0} received : {1}", name, text);
        }
    }
}