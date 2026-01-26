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
