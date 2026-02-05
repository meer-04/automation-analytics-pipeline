from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd

# CSV load
df = pd.read_csv("../data/raw/account_data.csv")
print("CSV loaded successfully. Columns: ", df.columns)

# Create output directory if it doesn't exist
BASE_DIR = Path(__file__).resolve().parent
output_dir = BASE_DIR.parent / "output" / "analysis"
output_dir.mkdir(parents=True, exist_ok=True)

# Basic cleansing
currency_columns = ["Account Balance", "Debit Amount", "Credit Amount"]
for col in currency_columns:
    clean_col = f"{col}_Clean"
    df[clean_col] = (
        df[col]
        .astype(str)
        .str.replace("$", "", regex=False)
        .str.replace(",", "", regex=False)
        .replace(["", "nan", "None"], pd.NA)
        .astype(float)
    )

print("Currency column cleaned successfully: ", df.columns)
print(df[[f"{currency_columns[0]}_Clean"]].head())

date_column = "Transaction Date"
df[date_column] = pd.to_datetime(
    df[date_column],
    errors="coerce"  # invalid or blank dates → NULL
)
print("Date column parsed successfully.")
print(df[[date_column]].head())

# Basic details
total_transactions = len(df)
total_credit = df["Credit Amount_Clean"].sum(skipna=True)
total_debit = df["Debit Amount_Clean"].sum(skipna=True)
net_cash_flow = total_credit - total_debit
print("\n--- BASIC TRANSACTION METRICS ---")
print(f"Total transactions      : {total_transactions}")
print(f"Total credit amount     : {total_credit}")
print(f"Total debit amount      : {total_debit}")
print(f"Net cash flow           : {net_cash_flow}")

# Charts
# Chart 1: Credit vs Debit
labels = ["Credit", "Debit"]
values = [total_credit, total_debit]

plt.figure(figsize=(6, 4))
plt.bar(labels, values, color=["gold", "salmon"])
plt.title("Credit vs Debit Amount")
plt.ylabel("Amount")
plt.xlabel("Transaction Type")
plt.tight_layout()
plt.savefig(output_dir / "credit_vs_debit.png")
plt.close()
print("Chart saved: credit_vs_debit.png")

# Chart 2: Debit Amount Over Time
debit_per_day = (
    df[df["Debit Amount_Clean"].notna() & df["Transaction Date"].notna()]
    .groupby("Transaction Date", as_index=False)["Debit Amount_Clean"]
    .sum()
    .sort_values("Transaction Date")
)

plt.figure(figsize=(8, 4))
plt.plot(
    debit_per_day["Transaction Date"],
    debit_per_day["Debit Amount_Clean"],
    marker="o"
)

plt.title("Daily Debit Amount Over Time")
plt.xlabel("Date")
plt.ylabel("Total Debit Amount")

plt.xticks(rotation=45)
plt.tight_layout()
plt.savefig(output_dir / "debit_over_time.png")
plt.close()

print("Chart saved: debit_over_time.png")


# High-Value Transaction Detection
HIGH_VALUE_THRESHOLD = 5000

high_value_debits = df[
    df["Debit Amount_Clean"].notna() &
    (df["Debit Amount_Clean"] >= HIGH_VALUE_THRESHOLD)
    ]

print("\n--- HIGH-VALUE DEBIT TRANSACTIONS ---")
print(f"Threshold used: {HIGH_VALUE_THRESHOLD}")
print(f"Count: {len(high_value_debits)}")

print(
    high_value_debits[
        ["Transaction Date", "Debit Amount_Clean"]
    ].sort_values("Debit Amount_Clean", ascending=False)
)


# HTML Dashboard
dashboard_path = output_dir / "dashboard.html"
html_content = f"""
<!DOCTYPE html>
<html>
<head>
    <title>Transaction Analysis Dashboard</title>
    <style>
        body {{
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f9f9f9;
        }}
        h1 {{
            color: #333;
        }}
        .metrics {{
            display: flex;
            gap: 20px;
            margin-bottom: 30px;
        }}
        .card {{
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            min-width: 200px;
        }}
        img {{
            margin-top: 20px;
            max-width: 800px;
        }}
    </style>
</head>
<body>

<h1>Transaction Analysis Dashboard</h1>
<p>Automated analysis generated from transaction data.</p>

<div class="metrics">
    <div class="card">
        <h3>Total Transactions</h3>
        <p>{total_transactions}</p>
    </div>
    <div class="card">
        <h3>Total Credit</h3>
        <p>{total_credit}</p>
    </div>
    <div class="card">
        <h3>Total Debit</h3>
        <p>{total_debit}</p>
    </div>
    <div class="card">
        <h3>Net Cash Flow</h3>
        <p>{net_cash_flow}</p>
    </div>
</div>

<h2>Credit vs Debit</h2>
<img src="credit_vs_debit.png" alt="Credit vs Debit">

<h2>Daily Debit Over Time</h2>
<img src="debit_over_time.png" alt="Debit Over Time">

<div style="background-color: #D32F2F; color: white; padding: 20px; border-radius: 5px;">
<h2 style="margin-top: 0;">High-Value Transaction Summary</h2>
<p>Threshold used: {HIGH_VALUE_THRESHOLD}</p>
<p>High-value debit transactions found: {len(high_value_debits)}</p>
</div>

</body>
</html>
"""

with open(dashboard_path, "w", encoding="utf-8") as f:
    f.write(html_content)

print(f"Dashboard generated: {dashboard_path}")
